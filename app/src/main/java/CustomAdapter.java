//CustomAdapter

package com.example.restart;

import com.bumptech.glide.Glide;
import com.example.restart.CustomAdapter;
import com.example.restart.ItemData;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.restart.ItemData;

import java.util.ArrayList;
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
        FrameLayout imageBackground = convertView.findViewById(R.id.item_image_background);

        FrameLayout call_button = convertView.findViewById(R.id.button1_container);
        FrameLayout share_button = convertView.findViewById(R.id.button2_container);
        FrameLayout delete_button = convertView.findViewById(R.id.button3_container);

        // 데이터 설정
//        Glide.with(context)
//                .load(currentItem.getImageURL())
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.error)
//                .into(imageView);

        textView1.setText(currentItem.getText1());
        textView2.setText(currentItem.getText2());

        switch (currentItem.getType().toLowerCase()) {
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

        call_button.setOnClickListener(v -> {
            String phone = textView2.getText().toString();
            if (!phone.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "전화번호가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        share_button.setOnClickListener(v -> {
            String name = textView1.getText().toString();
            String phoneNum = textView2.getText().toString();
            String shareContent = "음식점 이름: " + name + "\n전화번호: " + phoneNum;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            context.startActivity(Intent.createChooser(shareIntent, "공유하기"));
        });

        return convertView;
    }

    public void updateData(List<ItemData> newItems) {
        try {

            List<ItemData> newDataCopy = new ArrayList<>(newItems);
            Log.d("Load D3123123ata", "Loaded JSON: " + newItems);
            this.items.clear(); // 기존 데이터 제거
            Log.d("Load D3123123ata", "Loaded JSON: " + newItems);
            this.items.addAll(newDataCopy); // 새로운 데이터 추가
            Log.d("Load D3123123ata", "Loaded JSON: " + newItems);
            notifyDataSetChanged(); // 어댑터 갱신
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
