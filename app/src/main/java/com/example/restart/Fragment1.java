//Fragement

package com.example.restart;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.restart.databinding.Fragment1Binding;
import com.example.restart.model.RestaurantData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//add for type 1
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class Fragment1 extends Fragment {

    public Fragment1() {
        //
        //
    }

    private Fragment1Binding binding;

    private com.example.restart.CustomAdapter adapter;
    private List<com.example.restart.ItemData> data;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment1Binding.inflate(inflater, container, false);

        copyJsonToInternalStorage();

        binding.button12.setOnClickListener(v -> navigateToFragment2());
        binding.button13.setOnClickListener(v -> navigateToFragment3());

        ListView list = binding.list;

        // 모든 데이터 로드
        data = loadAllRestaurants();

        // 커스텀 어댑터 설정
        adapter = new com.example.restart.CustomAdapter(requireContext(), data);
        list.setAdapter(adapter);


        //추가 기능
        binding.buttonAddRestaurant.setOnClickListener(v -> {
            AddRestaurantDialog dialog = new AddRestaurantDialog();
            dialog.show(getParentFragmentManager(), "AddRestaurantDialog");
        });


        //삭제 기능
        list.setOnItemClickListener((parent, view, position, id) -> {
            // 삭제 여부를 묻는 팝업 띄우기
            showDeleteConfirmationDialog(position);
        });


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
        }

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
            String[] files = {"restaurants.json", "added_restaurants.json"};
            for (String fileName : files) {
                InputStream is;
                File outputFile = new File(requireContext().getFilesDir(), fileName);

                if (!outputFile.exists()) {
                    if (fileName.equals("added_restaurants.json")) {
                        // 빈 JSON 파일 생성
                        FileOutputStream fos = new FileOutputStream(outputFile);
                        fos.write("[]".getBytes()); // 빈 리스트로 초기화
                        fos.close();
                    } else {
                        // assets에서 복사
                        is = requireContext().getAssets().open(fileName);
                        FileOutputStream fos = new FileOutputStream(outputFile);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                        fos.close();
                        is.close();
                    }
                }
                Log.d("File Copy", fileName + " exists: " + outputFile.exists());
            }
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
            Log.d("Read JSON", "Content of restaurants.json: " + json);

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

            // `data`를 `List<Restaurant>`로 변환
            List<RestaurantData.Restaurant> restaurantList = new ArrayList<>();
            for (com.example.restart.ItemData item : data) {
                RestaurantData.Restaurant restaurant = new RestaurantData.Restaurant();
                restaurant.setName(item.getText1()); // `ItemData`의 필드에 맞게 설정
                restaurant.setPhone(item.getText2());
                restaurant.setImage(item.getImageURL());
                restaurant.setType(item.getType());
                restaurantList.add(restaurant);
                Log.d("Save Data List", "Data list saved to: " + item.getText1());
            }


            // `RestaurantData` 객체 생성 및 설정
            RestaurantData restaurantData = new RestaurantData();
            restaurantData.setRestaurants(restaurantList);

            // JSON으로 변환
            String jsonData = gson.toJson(restaurantData);

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
            Log.d("Load Restaurants", "Calling readJsonFromInternalStorage()"); // 호출 로그

            String jsonRestaurants = readJsonFromFile("restaurants.json");
            if (jsonRestaurants != null) {
                Log.d("Read JSON", "Content of restaurants.json: " + jsonRestaurants);
                Gson gson = new Gson();
                Type type = new TypeToken<RestaurantData>() {}.getType();
                RestaurantData restaurantData = gson.fromJson(jsonRestaurants, type);

                for (RestaurantData.Restaurant restaurant : restaurantData.getRestaurants()) {
                    data.add(new com.example.restart.ItemData(
                            restaurant.getImage(),
                            restaurant.getName(),
                            restaurant.getPhone(),
                            restaurant.getType()
                    ));
                }
            } else {
                Log.e("Load Restaurants", "Failed to load restaurants.json.");
            }
            // 추가된 added_restaurants.json 읽기
            String jsonAdded = readJsonFromFile("added_restaurants.json");
            if (jsonAdded != null) {
                Log.d("Read JSON", "Content of added_restaurants.json: " + jsonAdded);
                Gson gson = new Gson();
                Type type = new TypeToken<List<com.example.restart.ItemData>>() {}.getType();
                List<com.example.restart.ItemData> addedData = gson.fromJson(jsonAdded, type);

                for (com.example.restart.ItemData item : addedData) {
                    if (!data.contains(item)) { // 중복 데이터 확인
                        data.add(item);
                    }
                }
            }else {
                Log.e("Load Restaurants", "Failed to load added_restaurants.json.");
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
            Log.d("Read JSON", "Attempting to read: " + file.getAbsolutePath());
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
            Log.d("Read JSON", "Successfully read " + fileName);
            return jsonBuilder.toString();
        } catch (Exception e) {
            Log.e("Read JSON", "Error reading " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
        return "[]";
    }


    //AddRestaurantListener 구현
    public class RestaurantFragment extends Fragment implements AddRestaurantDialog.AddRestaurantListener {

        private Fragment1Binding binding;

        private com.example.restart.CustomAdapter adapter;
        private List<com.example.restart.ItemData> data;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            binding = Fragment1Binding.inflate(inflater, container, false);

            binding.button12.setOnClickListener(v -> navigateToFragment2());
            binding.button13.setOnClickListener(v -> navigateToFragment3());

            ListView list = binding.list;

            // 데이터 로드
            data = loadAllRestaurants();

            // 어댑터 설정
            adapter = new com.example.restart.CustomAdapter(requireContext(), data);
            list.setAdapter(adapter);

            binding.buttonAddRestaurant.setOnClickListener(v -> {
                AddRestaurantDialog dialog = new AddRestaurantDialog();
                dialog.show(getParentFragmentManager(), "AddRestaurantDialog");
            });

            return binding.getRoot();
        }

        @Override
        public void onRestaurantAdded(String name, String phone, String imageURL, String type) {
            addNewRestaurant(name, phone, imageURL, type); // 데이터 추가
            saveDataListToFile(); // JSON 저장
            adapter.updateData(data); // UI 업데이트
        }
    }

    private void showDeleteConfirmationDialog(int position) {

        Log.d("check1","test");
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Restaurant")
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("네", (dialog, which) -> {
                    // 항목 삭제

                    Log.d("check2","test");
                    deleteRestaurant(position);
                })
                .setNegativeButton("아니요", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }
    private void loadDataFromFile() {
        try {
            File file = new File(requireContext().getFilesDir(), "added_restaurants.json");
            if (file.exists()) {
                Gson gson = new Gson();
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();

                String jsonData = jsonBuilder.toString();
                Log.d("Load Data", "Loaded JSON: " + jsonData);

                // JSON 문자열을 List<ItemData>로 변환
                Type type = new TypeToken<List<com.example.restart.ItemData>>() {}.getType();
                List<com.example.restart.ItemData> loadedData = gson.fromJson(jsonData, type);

                // 기존 데이터와 동기화
                if (loadedData != null) {
                    data.clear();
                    data.addAll(loadedData);
                    Log.d("Load Data", "Data successfully loaded and updated.");
                }
            } else {
                Log.d("Load Data", "File not found: " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            Log.e("Load Data", "Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void deleteRestaurant(int position) {
        try {
            // 1. 삭제 대상 항목 가져오기
            String nameToDelete = data.get(position).getText1(); // 항목 이름 가져오기

            // 2. 메인 데이터에서 항목 삭제
            data.remove(position);

            // 3. restaurants.json에서 항목 삭제
            removeFromJsonFile("restaurants.json", nameToDelete);

            // 4. added_restaurants.json에서 항목 삭제
            removeFromJsonFile("added_restaurants.json", nameToDelete);

            // 5. 어댑터 갱신
            adapter.updateData(data);

            Log.d("Delete Restaurant", "Restaurant deleted: " + nameToDelete);
        } catch (Exception e) {
            Log.e("Delete Restaurant", "Error deleting restaurant: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void removeFromJsonFile(String fileName, String nameToDelete) {
        try {
            // 파일 읽기
            String json = readJsonFromFile(fileName);
            if (json == null) return;

            Gson gson = new Gson();
            if (fileName.equals("restaurants.json")) {
                // restaurants.json 삭제 로직
                Type type = new TypeToken<RestaurantData>() {}.getType();
                RestaurantData restaurantData = gson.fromJson(json, type);

                // 이름 일치 항목 제거
                List<RestaurantData.Restaurant> filteredList = new ArrayList<>();
                for (RestaurantData.Restaurant restaurant : restaurantData.getRestaurants()) {
                    if (!restaurant.getName().equals(nameToDelete)) {
                        filteredList.add(restaurant);
                    }
                }

                // 갱신된 리스트 저장
                restaurantData.setRestaurants(filteredList);
                writeJsonToInternalStorage(gson.toJson(restaurantData), fileName);

            } else if (fileName.equals("added_restaurants.json")) {
                // added_restaurants.json 삭제 로직
                Type type = new TypeToken<List<com.example.restart.ItemData>>() {}.getType();
                List<com.example.restart.ItemData> itemList = gson.fromJson(json, type);

                // 이름 일치 항목 제거
                List<com.example.restart.ItemData> filteredList = new ArrayList<>();
                for (com.example.restart.ItemData item : itemList) {
                    if (!item.getText1().equals(nameToDelete)) {
                        filteredList.add(item);
                    }
                }

                // 갱신된 리스트 저장
                writeJsonToInternalStorage(gson.toJson(filteredList), fileName);
            }
        } catch (Exception e) {
            Log.e("Remove From JSON", "Error removing from " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeJsonToInternalStorage(String jsonString, String fileName) {
        try {
            File file = new File(requireContext().getFilesDir(), fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonString.getBytes());
            fos.close();
            Log.d("Write JSON", "File updated: " + fileName);
        } catch (Exception e) {
            Log.e("Write JSON", "Error writing to " + fileName + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleNewRestaurant(String name, String phone, String imageURL, String type) {
        addNewRestaurant(name, phone, imageURL, type);
        saveDataListToFile();
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
