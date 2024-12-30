package com.example.restart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.restart.ItemData;
import com.example.restart.R;

import java.util.List;

public class CustomAdapter_tab2 extends BaseAdapter {
    private Context context;
    private List<com.example.restart.RestaurantData_tab2.Restaurant> items;

    // 생성자
    public CustomAdapter_tab2(Context context, List<com.example.restart.RestaurantData_tab2.Restaurant> items) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false);
        }

        // 현재 데이터 가져오기
        com.example.restart.RestaurantData_tab2.Restaurant currentItem = items.get(position);

        // 레이아웃의 뷰 참조
        ImageView imageView = convertView.findViewById(R.id.item_image);
        TextView textView1 = convertView.findViewById(R.id.item_text1);
        TextView textView2 = convertView.findViewById(R.id.item_text2);

        // 데이터 설정
        Glide.with(context)
                .load(currentItem.getImageURL())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .override(150,150)
                .into(imageView);

        textView1.setText(currentItem.getName());
        textView2.setText(currentItem.getComment());

        double latitude = currentItem.getLatitude();
        double longitude = currentItem.getLongitude();

        return convertView;
    }
}
