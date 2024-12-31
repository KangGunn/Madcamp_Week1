package com.example.restart.model;

import java.util.List;

public class RestaurantData {
    private List<Restaurant> items;

    public List<Restaurant> getRestaurants() {
        return items;
    }

    public void setRestaurants(List<Restaurant> items) { // `items` 리스트를 설정
        this.items = items;
    }

    public static class Restaurant {
        private String name;
        private String phone;
        private String img;
        private String type;

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getImage() {
            return img;
        }

        public String getType() {
            return type;
        }

        // Setter 메서드
        public void setName(String name) {
            this.name = name;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setImage(String img) {
            this.img = img;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
