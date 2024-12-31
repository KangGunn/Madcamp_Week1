//Fragement

package com.example.restart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.restart.CustomAdapter;
import com.example.restart.databinding.Fragment1Binding;
import com.example.restart.model.RestaurantData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.GenericSignatureFormatError;
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

    private CustomAdapter adapter;
    private List<com.example.restart.ItemData> data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment1Binding.inflate(inflater, container, false);

        binding.button12.setOnClickListener(v -> navigateToFragment2());
        binding.button13.setOnClickListener(v -> navigateToFragment3());

        ListView list = binding.list;

        // 모든 데이터 로드
        data = loadAllRestaurants();

        // 커스텀 어댑터 설정
        adapter = new CustomAdapter(requireContext(), data);
        list.setAdapter(adapter);

        binding.buttonAddRestaurant.setOnClickListener(v -> {
            addNewRestaurant("New Restauran!@#!@#t", "123-456-7890", "https://example.com/image.jpg", "cafe");
            saveDataListToFile();
        });


        return binding.getRoot();
    }


    //Json -> data로 전달
    private List<com.example.restart.ItemData> loadRestaurantsFromJson(){
        List<com.example.restart.ItemData> data = new ArrayList<>();
        try{
            //read Json from assets folder
            InputStreamReader isr = new InputStreamReader(requireContext().getAssets().open("restaurants.json"));
            BufferedReader reader = new BufferedReader(isr);

            //passing Json to GSON
            Gson gson = new Gson();
            Type type = new TypeToken<RestaurantData>() {}.getType();
            RestaurantData restaurantData = gson.fromJson(reader,type);

            // JSON -> ItemData
            for (RestaurantData.Restaurant restaurant : restaurantData.getRestaurants()) {
                data.add(new com.example.restart.ItemData(restaurant.getImage(), restaurant.getName(), restaurant.getPhone(), restaurant.getType()));
            }
            //예외 발생시
        } catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    // JSON 파일을 내부 저장소로 복사
    private void copyJsonToInternalStorage() {
        try {
            InputStream is = requireContext().getAssets().open("restaurants.json");
            File outputFile = new File(requireContext().getFilesDir(), "restaurants.json");

            if (!outputFile.exists()) {
                FileOutputStream fos = new FileOutputStream(outputFile);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
            }
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // JSON 파일 읽기
    private String readJsonFromInternalStorage() {
        try {
            File file = new File(requireContext().getFilesDir(), "restaurants.json");
            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            reader.close();

            String json = jsonBuilder.toString();

            return jsonBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // JSON 파일 쓰기
    private void writeJsonToInternalStorage(String jsonString) {
        try {
            File file = new File(requireContext().getFilesDir(), "restaurants.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonString.getBytes());
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addNewRestaurant(String name, String phone, String imageURL, String type) {
        try {
            // JSON 읽기
            String json = readJsonFromInternalStorage();
            if (json == null) return;

            // JSON 파싱
            Gson gson = new Gson();
            Type typeToken = new TypeToken<RestaurantData>() {}.getType();
            RestaurantData restaurantData = gson.fromJson(json, typeToken);

            // 새로운 데이터 추가
            RestaurantData.Restaurant newRestaurant = new RestaurantData.Restaurant();
            newRestaurant.setName(name);
            newRestaurant.setPhone(phone);
            newRestaurant.setImage(imageURL);
            newRestaurant.setType(type);
            restaurantData.getRestaurants().add(newRestaurant);

            // JSON 파일에 저장
            String updatedJson = gson.toJson(restaurantData);
            writeJsonToInternalStorage(updatedJson);

            // UI 업데이트
            List<com.example.restart.ItemData> data = new ArrayList<>();
            for (RestaurantData.Restaurant restaurant : restaurantData.getRestaurants()) {
                data.add(new com.example.restart.ItemData(restaurant.getImage(), restaurant.getName(), restaurant.getPhone(), restaurant.getType()));
            }
            adapter.updateData(data); // 어댑터 업데이트

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveDataListToFile() {
        try {
            // 내부 저장소 파일 경로
            File file = new File(requireContext().getFilesDir(), "added_restaurants.json");
            Gson gson = new Gson();

            // `data` 리스트를 JSON으로 변환
            String jsonData = gson.toJson(data);

            // 파일로 저장
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonData.getBytes());
            fos.close();

            Log.d("Save Data List", "Data list saved to: " + file.getAbsolutePath());
        } catch (Exception e) {
            Log.e("Save Data List", "Error saving data list: " + e.getMessage());
            e.printStackTrace();
        }
    }


    //Load All Restaurant Data
    private List<com.example.restart.ItemData> loadAllRestaurants() {
        List<com.example.restart.ItemData> data = new ArrayList<>();
        try {

            // 추가된 added_restaurants.json 읽기
            String jsonAdded = readJsonFromFile("added_restaurants.json");
            if (jsonAdded != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<com.example.restart.ItemData>>() {}.getType();
                List<com.example.restart.ItemData> addedData = gson.fromJson(jsonAdded, type);

                data.addAll(addedData); // 추가 데이터 병합
            }
        } catch (Exception e) {
            Log.e("Load All Restaurants", "Error loading restaurants: " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

    //readJsonFromFile
    private String readJsonFromFile(String fileName) {
        try {
            File file = new File(requireContext().getFilesDir(), fileName);
            if (!file.exists()) {
                Log.d("Read JSON", fileName + " does not exist.");
                return null;
            }
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
            Log.e("Read JSON", "Error reading " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
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
