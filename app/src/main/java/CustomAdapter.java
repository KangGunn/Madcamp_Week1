package com.example.restart;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<com.example.restart.ItemData> items;

    // 생성자
    public CustomAdapter(Context context, List<com.example.restart.ItemData> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size(); // 데이터의 개수 반환
    }

    @Override
    public Object getItem(int position) {
        return items.get(position); // 특정 위치의 데이터 반환
    }

    @Override
    public long getItemId(int position) {
        return position; // 아이템 ID 반환 (여기서는 position 사용)
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        // 현재 데이터 가져오기
        com.example.restart.ItemData currentItem = items.get(position);

        // 첫 번째 ItemData.Item 가져오기
        com.example.restart.ItemData.Item firstItem = currentItem.getItems() != null && !currentItem.getItems().isEmpty()
                ? currentItem.getItems().get(0) // 첫 번째 아이템 가져오기
                : null;

        // 레이아웃의 뷰 참조
        ImageView imageView = convertView.findViewById(R.id.item_image);
        TextView textView1 = convertView.findViewById(R.id.item_text1);
        TextView textView2 = convertView.findViewById(R.id.item_text2);
        FrameLayout imageBackground = convertView.findViewById(R.id.item_image_background);

        // 데이터 설정
        if (firstItem != null) {
            textView1.setText(firstItem.getPhone());
            textView2.setText(firstItem.getName());

            switch (firstItem.getType()) {
                case "restaurant":
                    imageBackground.setBackgroundResource(R.drawable.restaurant_round);
                    imageView.setImageResource(R.drawable.restaurant_icon);
                    break;
                case "pub":
                    imageBackground.setBackgroundResource(R.drawable.pub_round);
                    imageView.setImageResource(R.drawable.pub_icon);
                    break;
                case "cafe":
                    imageBackground.setBackgroundResource(R.drawable.cafe_round);
                    imageView.setImageResource(R.drawable.cafe_icon);
                    break;
            }
        }

        return convertView;
    }
}