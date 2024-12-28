package com.example.restart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.restart.databinding.Fragment2Binding;
import com.example.restart.model.RestaurantData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

//add for type 2
import com.example.restart.model.RestaurantData_tab2;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Fragment2 extends Fragment {

    public Fragment2() {
        //
    }

    private Fragment2Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment2Binding.inflate(inflater, container, false);
        binding.button21.setOnClickListener(v -> navigateToFragment1());
        binding.button23.setOnClickListener(v -> navigateToFragment3());

        GridView grid = binding.grid;

        List<com.example.restart.ItemData> data = loadRestaurantsFromJson();

        com.example.restart.CustomAdapter adapter = new com.example.restart.CustomAdapter(requireContext(), data);
        grid.setAdapter(adapter);

        return binding.getRoot();
    }

    private List<com.example.restart.ItemData> loadRestaurantsFromJson(){
        List<com.example.restart.ItemData> data = new ArrayList<>();
        try{
            //read Json from assets folder
            InputStreamReader isr = new InputStreamReader(requireContext().getAssets().open("restaurants_tab2.json"));
            BufferedReader reader = new BufferedReader(isr);

            //passing Json to GSON
            Gson gson = new Gson();
            Type type = new TypeToken<RestaurantData_tab2>() {}.getType();
            RestaurantData_tab2 restaurantData = gson.fromJson(reader,type);

            // JSON -> ItemData
            for (RestaurantData_tab2.Restaurant restaurant : restaurantData.getRestaurants()) {
                data.add(new com.example.restart.ItemData(restaurant.getImageUrl(), restaurant.getName(), restaurant.getPhone()));
            }
            //예외발생시
        } catch (Exception e){
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
