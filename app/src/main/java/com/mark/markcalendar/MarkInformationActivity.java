package com.mark.markcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MarkInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_information);

        //仮
        ArrayList<MarkTable> marks = new ArrayList<>();

        //レイアウトからリストビューを取得
        RecyclerView rv_markList = findViewById(R.id.rv_markList);
        //アダプタの生成
        MarkListAdapter adapter = new MarkListAdapter(marks);
        //アダプタの設定
        rv_markList.setAdapter(adapter);
        //レイアウトマネージャの設定
        rv_markList.setLayoutManager( new LinearLayoutManager(this) );



    }
}