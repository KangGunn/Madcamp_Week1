package com.example.restart;
public class ItemData {
    private int imageResId;  // 이미지 리소스 ID
    private String text1;    // 텍스트1
    private String text2;    // 텍스트2

    // 생성자
    public ItemData(int imageResId, String text1, String text2) {
        this.imageResId = imageResId;
        this.text1 = text1;
        this.text2 = text2;
    }

    // Getter 메서드
    public int getImageResId() {
        return imageResId;
    }

    public String getText1() {
        return text1;
    }

    public String getText2() {
        return text2;
    }
}


