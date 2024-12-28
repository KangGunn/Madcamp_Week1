package com.example.restart.model;

import java.util.List;

public class RestaurantData {
    private List<Restaurant> restaurants;

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public static class Restaurant {
        private String name;
        private String phone;

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }
    }
}