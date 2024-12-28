package com.example.restart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.restart.databinding.Fragment3Binding;

public class Fragment3 extends Fragment {

    public Fragment3() {
        //
    }

    private Fragment3Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = Fragment3Binding.inflate(inflater, container, false);
        binding.button31.setOnClickListener(v -> navigateToFragment1());
        binding.button32.setOnClickListener(v -> navigateToFragment2());

        String imageUrl = "https://picsum.photos/400/300";
        loadImage(imageUrl);

        return binding.getRoot();
    }

    private void loadImage(String url){
        Glide.with(this)
                .load(url)
                .into(binding.imageView);
    }

    private void navigateToFragment1() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(Fragment3Directions.Companion.actionFragment3ToFragment1());
    }

    private void navigateToFragment2() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(Fragment3Directions.Companion.actionFragment3ToFragment2());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
