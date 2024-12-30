package com.example.restart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment2Binding.inflate(inflater, container, false);

        // Navigation Buttons
        binding.button21.setOnClickListener(v -> navigateToFragment1());
        binding.button23.setOnClickListener(v -> navigateToFragment3());

        // GridView setup
        GridView grid = binding.grid;

        List<com.example.restart.RestaurantData_tab2.Restaurant> data = loadRestaurantsFromJson();

        com.example.restart.CustomAdapter_tab2 adapter = new com.example.restart.CustomAdapter_tab2(requireContext(), data);
        grid.setAdapter(adapter);

        // GridView item click listener
        grid.setOnItemClickListener((parent, view, position, id) -> {
            com.example.restart.RestaurantData_tab2.Restaurant selectedItem = data.get(position);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
