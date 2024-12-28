package com.example.restart;
public class ItemData {
    private String imageURL;  // 이미지 리소스 ID
    private String text1;    // 텍스트1
    private String text2;    // 텍스트2

    // 생성자
    public ItemData(String imageURL, String text1, String text2) {
        this.imageURL = imageURL;
        this.text1 = text1;
        this.text2 = text2;
    }

    // Getter 메서드
    public String getImageURL() {
        return imageURL;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }
}


