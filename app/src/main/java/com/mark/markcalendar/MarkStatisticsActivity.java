package com.mark.markcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * マーク統計情報画面
 */
public class MarkStatisticsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_statistics);

        final int NO_MARK = -1;

        Intent intent = getIntent();
        int markPid = intent.getIntExtra( MarkListActivity.KEY_MARK, NO_MARK );
        if( markPid == NO_MARK ){
            //フェールセーフ
            finish();
        }

        //
        //DB読み込み処理
        //★マークリストは不要
        AsyncReadMark db = new AsyncReadMark(this, markPid, new AsyncReadMark.onFinishListener() {

            @Override
            public void onFinish(MarkArrayList<MarkTable> marks, MarkDateArrayList<MarkDateTable> markDates) {
                //月毎リストを作成
                MonthMarkArrayList<MonthMarkInformation> monthData = markDates.createMonthMarkData();

                //合計マーク数
                int total = markDates.size();

                //月ごとのマーク情報を表示
                RecyclerView rv_monthList = findViewById(R.id.rv_monthList);
                MonthMarkAdapter adapter = new MonthMarkAdapter( monthData, total );
                rv_monthList.setAdapter(adapter);
                rv_monthList.setLayoutManager( new LinearLayoutManager(rv_monthList.getContext()) );

                //合計マーク数を表示
                ((TextView)findViewById(R.id.tv_total)).setText( Integer.toString(total) );
            }
        });
        //非同期処理開始
        db.execute();

    }
}