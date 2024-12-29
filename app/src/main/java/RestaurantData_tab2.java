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
        private String address; // 위치 정보 추가

        // Getters
        public String getName() {
            return name;
        }

        public String getImage() {
            return image;
        }

        public String getComment() {
            return comment;
        }

        public String getAddress() {
            return address;
        }
    }
}
