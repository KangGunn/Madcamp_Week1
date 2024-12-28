package com.example.restart.model;

import  java.util.List;
public class RestaurantData_tab2 {
    private List<Restaurant> restaurants;

    public List<Restaurant> getRestaurants(){
        return  restaurants;
    }

    public static  class Restaurant{
        private String name;
        private String comment;
        private String imageUrl;

        public String getName(){
            return name;
        }

        public String getComment(){
            return comment;
        }

        public String getImage(){
            return imageUrl;
        }
    }


}
