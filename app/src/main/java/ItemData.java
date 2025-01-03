

package com.example.restart;
public class ItemData {
    private String imageURL;  // 이미지 리소스 ID
    private String text1;    // 텍스트1
    private String text2;    // 텍스트2
    private String type;     // 음식점 종류
    private double latitude; //경도
    private double longitude; //위도

    // 생성자
    public ItemData(String imageURL, String text1, String text2, String type) {
        this.imageURL = imageURL;
        this.text1 = text1;
        this.text2 = text2;
        this.type = type;
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
    public String getType() { return type; }
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}


