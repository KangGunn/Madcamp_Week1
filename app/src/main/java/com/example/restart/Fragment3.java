package com.example.restart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.restart.databinding.Fragment3Binding;
import com.example.restart.model.RestaurantData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Fragment3 extends Fragment {

    public Fragment3() {
        //
    }

    @Override
    public void onResume() {
        super.onResume();

        requireActivity().getWindow().setStatusBarColor(
                ContextCompat.getColor(requireContext(), R.color.main_color)
        );
    }

    private Fragment3Binding binding;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private Uri photoUri;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private final int[] buttonIds = {
            R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
            R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment3Binding.inflate(inflater, container, false);
        binding.button31.setOnClickListener(v -> navigateToFragment1());
        binding.button32.setOnClickListener(v -> navigateToFragment2());

        TextView clickPrompt = binding.clickPromptText;
        binding.imageView3.setOnClickListener(v -> showImagePickerDialog(clickPrompt));

        initPermissionLauncher(clickPrompt);
        initCameraLauncher(clickPrompt);
        initGalleryLauncher(clickPrompt);

        setupHashtagButtons();

        binding.loadButton.setOnClickListener(v -> {
            List<String> restaurantNames = getSavedRestaurantNames();

            if(restaurantNames.isEmpty()) {
                Toast.makeText(requireContext(), "저장된 음식점이 없습니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(requireContext())
                    .setItems(restaurantNames.toArray(new String[0]), ((dialog, which) -> {
                        binding.Titletext.setText(restaurantNames.get(which));
                    }))
                    .setNegativeButton("취소", null)
                    .create()
                    .show();
        });

        //add 12/29 - 업로드 버튼
        binding.uploadbutton.setOnClickListener(v -> showUploadDialog());

        return binding.getRoot();
    }

    private List<String> getSavedRestaurantNames() {
        List<String> restaurantNames = new ArrayList<>();
        try {
            String jsonRestaurants = readJsonFromFile("restaurants.json");
            if (jsonRestaurants != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<RestaurantData>() {}.getType();
                RestaurantData restaurantData = gson.fromJson(jsonRestaurants, type);

                for (RestaurantData.Restaurant restaurant : restaurantData.getRestaurants()) {
                    restaurantNames.add(restaurant.getName());
                }
            }

            String jsonAddedRestaurants = readJsonFromFile("added_restaurants.json");
            if (jsonAddedRestaurants != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<com.example.restart.ItemData>>() {}.getType();
                List<com.example.restart.ItemData> addedData = gson.fromJson(jsonAddedRestaurants, type);

                for (com.example.restart.ItemData item : addedData) {
                    restaurantNames.add(item.getText1());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restaurantNames;
    }

    private String readJsonFromFile(String fileName) {
        try {
            File file = new File(requireContext().getFilesDir(), fileName);
            if (!file.exists()) return null;

            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder jsonBuilder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();
            return jsonBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setupHashtagButtons() {
        for (int id : buttonIds) {
            Button button = binding.getRoot().findViewById(id);
            button.setOnClickListener(v -> {
                boolean isSelected = button.isSelected();
                button.setSelected(!isSelected);
            });
        }
    }

    private void showImagePickerDialog(TextView clickPrompt) {
        String[] options = {"카메라", "갤러리"};

        new AlertDialog.Builder(requireContext())
                .setTitle("이미지 선택")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) checkCameraPermissionAndOpenCamera(clickPrompt);
                    else if (which == 1) openGallery(clickPrompt);
                })
                .create()
                .show();
    }

    private void checkCameraPermissionAndOpenCamera(TextView clickPrompt) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        } else {
            openCamera(clickPrompt);
        }
    }

    private void initPermissionLauncher(TextView clickPrompt) {
        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openCamera(clickPrompt);
                    } else {
                        Log.e("Permissions", "Camera permission denied");
                    }
                }
        );
    }

    private void openCamera(TextView clickPrompt) {
        String fileName = "photo_" + System.currentTimeMillis() + ".jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        photoUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        if (photoUri != null) {
            cameraLauncher.launch(photoUri);
        } else {
            Log.e("Camera", "Failed to create media file");
        }
    }

    private void initCameraLauncher(TextView clickPrompt) {
        cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            isSuccess -> {
                if (isSuccess && photoUri != null) {
                    Glide.with(this)
                            .load(photoUri)
                            .into(binding.imageView3);
                    clickPrompt.setVisibility(View.GONE);
                } else {
                    Log.e("Camera", "Image capture failed or URI is null");
                }
            }
        );
    }

    private void openGallery(TextView clickPrompt) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void initGalleryLauncher(TextView clickPrompt) {
        galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        binding.imageView3.setImageURI(selectedImageUri);
                        clickPrompt.setVisibility(View.GONE);
                    }
                }
            }
        );
    }


    //add 1229 : 업로드 버튼 누를시 팝업
    //add 1229 : 업로드 버튼 누를시 팝업
    //add 1229 : 업로드 버튼 누를시 팝업
    private void showUploadDialog(){
        if (isInputValid()) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("업로드 확인")
                    .setMessage("이대로 업로드하시겠습니까?")
                    .setPositiveButton("확인", (dialog, which) -> {
                        //클릭시 코드실행
                        performUpload();
                    })
                    .setNegativeButton("취소", (dialog, which) -> {
                        //클릭시 코드실행
                    })
                    .create()
                    .show();
        } else {
            new AlertDialog.Builder(requireContext())
//                    .setTitle("작성 미완료")
                    .setMessage("이미지, 음식점 이름, 해시태그를\n모두 작성해주세요.")
                    .create()
                    .show();
        }
    }

    private boolean isInputValid() {
        if (binding.imageView3.getDrawable() == null) return false;
        if (binding.Titletext.getText().toString().trim().isEmpty()) return false;

        boolean flag = false;
        for (int id : buttonIds) {
            Button button = binding.getRoot().findViewById(id);
            if (button != null && button.isSelected()) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void performUpload() {
        Log.i("Upload", "업로드 작업이 완료되었습니다.");
        Toast.makeText(requireContext(), "업로드가 완료되었습니다!", Toast.LENGTH_SHORT).show();

        // Updated at 12.30
        // hashtag reset
        for (int id : buttonIds) {
            Button button = binding.getRoot().findViewById(id);
            if (button != null) {
                button.setSelected(false);
            }
        }
        // image reset
        binding.imageView3.setImageResource(0);
        binding.clickPromptText.setVisibility(View.VISIBLE);
        // text reset
        binding.Titletext.setText("");
    }


    private void navigateToFragment1() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(Fragment3Directions.Companion.actionFragment3ToFragment1());
    }

    private void navigateToFragment2() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(Fragment3Directions.Companion.actionFragment3ToFragment2());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
