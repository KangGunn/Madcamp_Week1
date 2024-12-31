package com.example.restart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.restart.databinding.Fragment2Binding;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Fragment2 extends Fragment {

    public Fragment2() {
        // Default constructor
    }

    private Fragment2Binding binding;
    List<com.example.restart.RestaurantData_tab2.Restaurant> data;
    com.example.restart.CustomAdapter_tab2 adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment2Binding.inflate(inflater, container, false);

        // Navigation Buttons
        binding.button21.setOnClickListener(v -> navigateToFragment1());
        binding.button23.setOnClickListener(v -> navigateToFragment3());

        // GridView setup
        GridView grid = binding.grid;

        data = loadRestaurantsFromJson();

        adapter = new com.example.restart.CustomAdapter_tab2(requireContext(), data);
        grid.setAdapter(adapter);

        binding.filter.setOnClickListener(v -> showFilterPopupMenu());

        // GridView item click listener
        grid.setOnItemClickListener((parent, view, position, id) -> {
            com.example.restart.RestaurantData_tab2.Restaurant selectedItem = data.get(position);

            Log.d("MapData", "Latitude: " + selectedItem.getLatitude() + ", Longitude: " + selectedItem.getLongitude() + ", Name: " + selectedItem.getName());

            // Transition to MapFragment with data
            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", selectedItem.getLatitude());
            bundle.putDouble("longitude", selectedItem.getLongitude());
            bundle.putString("name", selectedItem.getName());

            com.example.restart.MapFragment  mapFragment = new com.example.restart.MapFragment();
            mapFragment.setArguments(bundle);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.mapcontainer, mapFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return binding.getRoot();
    }

    private List<com.example.restart.RestaurantData_tab2.Restaurant> loadRestaurantsFromJson() {
        List<com.example.restart.RestaurantData_tab2.Restaurant> data = new ArrayList<>();
        try {
            // Read JSON from assets folder
            InputStreamReader isr = new InputStreamReader(requireContext().getAssets().open("restaurants_tab2.json"));
            BufferedReader reader = new BufferedReader(isr);

            Gson gson = new Gson();
            Type type = new TypeToken<com.example.restart.RestaurantData_tab2>() {}.getType();
            com.example.restart.RestaurantData_tab2 restaurantData = gson.fromJson(reader, type);

            // Populate data list with Restaurant objects
            for (com.example.restart.RestaurantData_tab2.Restaurant restaurant : restaurantData.getRestaurants()) {
                Log.d("RestaurantData", "Loaded restaurant: " + restaurant.getName()); // 디버깅 로그
                Log.d("GlideDebug", "Image URL: " + restaurant.getImageURL());
                data.add(restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    private void navigateToFragment1() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(Fragment2Directions.Companion.actionFragment2ToFragment1());
    }

    private void navigateToFragment3() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(Fragment2Directions.Companion.actionFragment2ToFragment3());
    }

    private void showFilterPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(requireContext(), binding.filter);
        popupMenu.getMenuInflater().inflate(R.menu.filter_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.filter_all) {
                filterData(null); // 모든 데이터 표시
                binding.filterText.setText("필터: 전체");
                return true;
            } else if (itemId == R.id.filter_restaurant) {
                filterData("restaurant"); // "restaurant" 타입만 표시
                binding.filterText.setText("필터: 음식점");
                return true;
            } else if (itemId == R.id.filter_cafe) {
                filterData("cafe"); // "cafe" 타입만 표시
                binding.filterText.setText("필터: 카페");
                return true;
            } else if (itemId == R.id.filter_pub) {
                filterData("pub"); // "pub" 타입만 표시
                binding.filterText.setText("필터: 주점");
                return true;
            } else {
                return false; // 이벤트를 처리하지 않음
            }
        });

        popupMenu.show();
    }

    private void filterData(String type) {
        List<com.example.restart.RestaurantData_tab2.Restaurant> filteredData = new ArrayList<>();
        if (type == null) filteredData.addAll(data);
        else {
            for (com.example.restart.RestaurantData_tab2.Restaurant restaurant : data) {
                if (restaurant.getType().equals(type)) filteredData.add(restaurant);
            }
        }

        adapter = new com.example.restart.CustomAdapter_tab2(requireContext(), filteredData);
        binding.grid.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
