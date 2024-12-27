package com.example.restart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.restart.databinding.Fragment1Binding;

public class Fragment1 extends Fragment {

    public Fragment1() {
        //
    }

    private Fragment1Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment1Binding.inflate(inflater, container, false);
        binding.button12.setOnClickListener(v -> navigateToFragment2());
        binding.button13.setOnClickListener(v -> navigateToFragment3());
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
