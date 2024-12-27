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

import java.util.ArrayList;
import java.util.List;

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

        // 데이터 준비
        List<com.example.restart.ItemData> data = new ArrayList<>();
        data.add(new com.example.restart.ItemData(R.drawable.image1, "음식점123", "Description 1"));
        data.add(new com.example.restart.ItemData(R.drawable.ic_launcher_foreground, "Item 2", "Description 2"));
        data.add(new com.example.restart.ItemData(R.drawable.ic_launcher_foreground, "Item 3", "Description 3"));
        data.add(new com.example.restart.ItemData(R.drawable.ic_launcher_foreground, "Item 4", "Description 4"));
        data.add(new com.example.restart.ItemData(R.drawable.ic_launcher_foreground, "Item 5", "Description 5"));
        data.add(new com.example.restart.ItemData(R.drawable.image1, "Item 1", "Description 1"));
        data.add(new com.example.restart.ItemData(R.drawable.ic_launcher_foreground, "Item 2", "Description 2"));
        data.add(new com.example.restart.ItemData(R.drawable.ic_launcher_foreground, "Item 3", "Description 3"));
        data.add(new com.example.restart.ItemData(R.drawable.ic_launcher_foreground, "Item 4", "Description 4"));
        data.add(new com.example.restart.ItemData(R.drawable.ic_launcher_foreground, "Item 5", "Description 5"));
        data.add(new com.example.restart.ItemData(R.drawable.image1, "Item 1", "Description 1"));

        // 커스텀 어댑터 설정
        CustomAdapter adapter = new CustomAdapter(requireContext(), data);
        list.setAdapter(adapter);

        return binding.getRoot();
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
