package com.example.restart;

import android.os.Bundle;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment1Binding.inflate(inflater, container, false);
        binding.button12.setOnClickListener(v -> navigateToFragment2());
        binding.button13.setOnClickListener(v -> navigateToFragment3());

        ListView list = binding.list;

        List<com.example.restart.ItemData> data = loadRestaurantsFromJson();



        // 커스텀 어댑터 설정
        CustomAdapter adapter = new CustomAdapter(requireContext(), data);
        list.setAdapter(adapter);

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
                data.add(new com.example.restart.ItemData(R.drawable.ic_launcher_foreground, restaurant.getName(), restaurant.getPhone()));
            }
            //예외발생시
        } catch (Exception e){
            e.printStackTrace();
        }
        return data;
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
