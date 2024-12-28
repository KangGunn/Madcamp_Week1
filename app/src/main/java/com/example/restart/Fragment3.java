package com.example.restart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.media.MediaDescrambler;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.restart.databinding.Fragment3Binding;

import org.w3c.dom.Text;

public class Fragment3 extends Fragment {

    public Fragment3() {
        //
    }

    private Fragment3Binding binding;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Uri> cameraLauncher;
    private Uri photoUri;
    private ActivityResultLauncher<Intent> galleryLauncher;

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

        return binding.getRoot();
    }

    private void setupHashtagButtons() {
        int[] buttonIds = {
                R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5,
                R.id.button6, R.id.button7, R.id.button8, R.id.button9, R.id.button10
        };

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
