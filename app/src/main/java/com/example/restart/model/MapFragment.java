package com.example.restart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView nameTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        nameTextView=rootView.findViewById(R.id.showtext);


        // SupportMapFragment 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapcontainer);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        Bundle args = getArguments();
        if (args != null) {
            double latitude = args.getDouble("latitude", 37.5665); // 기본값은 서울
            double longitude = args.getDouble("longitude", 126.9780); // 기본값은 서울
            String name = args.getString("name", "Unknown");
            Log.d("MapFragment_HH", "Received Latitude: " + latitude + ", Longitude: " + longitude + ", Name: " + name);

            //이름 표시
            nameTextView.setText(name);
            // 지도 표시
            showLocationOnMap(latitude, longitude, name);
        }

        return rootView;
    }

    private void showLocationOnMap(double latitude, double longitude, String name) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.showmap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                LatLng location = new LatLng(latitude, longitude);

                Log.d("showLocationOnMap", "Adding marker at Latitude: " + latitude + ", Longitude: " + longitude + ", Name: " + name);

                googleMap.addMarker(new MarkerOptions().position(location).title(name));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            });
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 기본 위치 설정 (예: 서울)
        LatLng seoul = new LatLng(37.566, 126.9784);
        mMap.addMarker(new MarkerOptions().position(seoul).title("서울"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10));
    }
}
