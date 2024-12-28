package com.example.restart;

import com.bumptech.glide.Glide;
import com.example.restart.CustomAdapter;
import com.example.restart.ItemData;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.restart.ItemData;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<ItemData> items;

    // 생성자
    public CustomAdapter(Context context, List<ItemData> items) {
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
        ItemData currentItem = items.get(position);

        // 레이아웃의 뷰 참조
        ImageView imageView = convertView.findViewById(R.id.item_image);
        TextView textView1 = convertView.findViewById(R.id.item_text1);
        TextView textView2 = convertView.findViewById(R.id.item_text2);

        // 데이터 설정
        Glide.with(context)
                .load(currentItem.getImageURL())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(imageView);

        textView1.setText(currentItem.getText1());
        textView2.setText(currentItem.getText2());

        return convertView;
    }
}
