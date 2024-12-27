package com.example.restart;

import android.os.Bundle;
import android.widget.ListView;
import com.example.restart.ItemData;
import com.example.restart.CustomAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list);

        // 데이터 준비
        List<ItemData> data = new ArrayList<>();
        data.add(new ItemData(R.drawable.image1, "음식점123", "Description 1"));
        data.add(new ItemData(R.drawable.ic_launcher_foreground, "Item 2", "Description 2"));
        data.add(new ItemData(R.drawable.ic_launcher_foreground, "Item 3", "Description 3"));
        data.add(new ItemData(R.drawable.ic_launcher_foreground, "Item 4", "Description 4"));
        data.add(new ItemData(R.drawable.ic_launcher_foreground, "Item 5", "Description 5"));
        data.add(new ItemData(R.drawable.image1, "Item 1", "Description 1"));
        data.add(new ItemData(R.drawable.ic_launcher_foreground, "Item 2", "Description 2"));
        data.add(new ItemData(R.drawable.ic_launcher_foreground, "Item 3", "Description 3"));
        data.add(new ItemData(R.drawable.ic_launcher_foreground, "Item 4", "Description 4"));
        data.add(new ItemData(R.drawable.ic_launcher_foreground, "Item 5", "Description 5"));
        data.add(new ItemData(R.drawable.image1, "Item 1", "Description 1"));

        // 커스텀 어댑터 설정
        CustomAdapter adapter = new CustomAdapter(this, data);
        list.setAdapter(adapter);
    }
}
