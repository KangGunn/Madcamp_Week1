package com.example.restart;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.restart.databinding.Fragment1Binding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//add for type 1
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Fragment1 extends Fragment {

    public Fragment1() {
        //
        //
    }

    private Fragment1Binding binding;

    private void debugJsonFile() {
        File file = new File(requireContext().getFilesDir(), "restaurants.json");
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                BufferedReader bufferedReader = new BufferedReader(reader);
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    builder.append(line);
                }
                Log.d("DEBUG", "JSON File Content: " + builder.toString());
            } catch (Exception e) {
                Log.e("DEBUG", "Error reading JSON file: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            Log.e("DEBUG", "JSON file not found in internal storage.");
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment1Binding.inflate(inflater, container, false);

        debugJsonFile();
        // `List<ItemData>` 데이터 로드
        List<com.example.restart.ItemData> data = loadRestaurantsFromJson();

        // 어댑터 설정
        com.example.restart.CustomAdapter adapter = new com.example.restart.CustomAdapter(requireContext(), data);
        binding.list.setAdapter(adapter);

        // ListView 클릭 리스너 추가
        binding.list.setOnItemClickListener((parent, view, position, id) -> {
            // 선택된 ItemData 객체
            com.example.restart.ItemData selectedData = data.get(position);

            // ItemData 내부의 첫 번째 Item으로 처리 (예제)
            if (selectedData.getItems() != null && !selectedData.getItems().isEmpty()) {
                com.example.restart.ItemData.Item firstItem = selectedData.getItems().get(0);

                // 전화 걸기
                makePhoneCall(firstItem.getPhone());
            } else {
                Toast.makeText(requireContext(), "전화번호가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return binding.getRoot();
    }


    //Json -> data로 전달
    private List<com.example.restart.ItemData> loadRestaurantsFromJson() {
        List<com.example.restart.ItemData.Item> data = new ArrayList<>();
        try {
            File file = new File(requireContext().getFilesDir(), "restaurants.json");
            if (!file.exists()) {
                Log.e("DEBUG", "File not found in internal storage.");
            } else {
                Log.d("DEBUG", "File exists in internal storage.");
            }
            if (file.exists()) {
                FileReader reader = new FileReader(file);
                Gson gson = new Gson();
                Type type = new TypeToken<com.example.restart.ItemData>() {}.getType();
                ItemData itemData = gson.fromJson(reader,type);
                com.example.restart.ItemData restaurantData = gson.fromJson(reader, type);

                if (restaurantData != null && restaurantData.getItems() != null) {
                    for (com.example.restart.ItemData.Item item : restaurantData.getItems()) {
                        Log.d("DEBUG", "Loaded Item: " + item.getName() + ", Phone: " + item.getPhone());
                    }
                    data.addAll(itemData.getItems());
                } else {
                    Log.e("DEBUG", "No items found in JSON.");
                }
                reader.close();
            } else {
                Log.e("DEBUG", "restaurants.json file does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }






    private void makePhoneCall(String phoneNumber) {
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // 전화번호 URI 생성
            Uri phoneUri = Uri.parse("tel:" + phoneNumber);

            // Intent 생성
            Intent callIntent = new Intent(Intent.ACTION_CALL, phoneUri);

            startActivity(callIntent);
        }
    }

    private void navigateToFragment2() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(Fragment1Directions.Companion.actionFragment1ToFragment2());
    }

    private void navigateToFragment3() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(Fragment1Directions.Companion.actionFragment1ToFragment3());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
