package com.example.restart;

import java.util.List;
public class ItemData {
    private List<Item> items; // JSON의 최상위 키 "items"와 매핑

    public List<Item> getItems() {
        return items; // "items" 데이터 반환
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item {
        private String img;
        private String name;
        private String phone;
        private String type;

        // 생성자
        public Item(String img, String name, String phone, String type) {
            this.img = img;
            this.name = name;
            this.phone = phone;
            this.type = type;
        }

        // Getter 메서드
        public String getImg() {
            return img;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }

        public String getType() {
            return type;
        }
    }
}
