package com.example.restart;

import java.util.List;

public class RestaurantData_tab2 {
    private List<Restaurant> restaurants;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public static class Restaurant {
        private String name;
        private String image;
        private String comment;
        private double latitude; // 위치 정보 추가(위도)
        private double longitude; // 위치 정보 추가(경도)

        // Getters
        public String getName() {
            return name;
        }

        public String getImageURL() {
            return image;
        }

        public String getComment() {
            return comment;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude(){
            return longitude;
        }

    }
}
