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

import com.example.restart.databinding.Fragment2Binding;

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
        return binding.getRoot();
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
