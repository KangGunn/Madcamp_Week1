package com.example.restart;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.example.restart.ItemData;
import com.example.restart.CustomAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private View selectedIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        selectedIndicator = findViewById(R.id.selected_indicator);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
                int destinationId = destination.getId();
                if (destinationId == R.id.fragment1) moveIndicatorToPosition(0);
                else if (destinationId == R.id.fragment2) moveIndicatorToPosition(1);
                else if (destinationId == R.id.fragment3) moveIndicatorToPosition(2);
            });
        }
    }

    private void moveIndicatorToPosition(int position) {
        int targetX = (int) (position * 120 * getResources().getDisplayMetrics().density);

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(selectedIndicator, "translationX", targetX);
        animatorX.setDuration(300);
        animatorX.start();
    }
}
