package com.mark.markcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;

/*
 * カレンダーActivity
 */
public class CalendarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //ツールバー設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("選択中マークを表示");
        setSupportActionBar(toolbar);

        //カレンダーの表示
        GridView calendarGridView = findViewById(R.id.calendarGridView);
        CalendarAdapter mCalendarAdapter = new CalendarAdapter(this);
        calendarGridView.setAdapter(mCalendarAdapter);



    }

    /*
     * ツールバーオプションメニュー生成
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);

        return true;
    }

    /*
     * ツールバーアクション選択
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //マークメニュー選択
            case R.id.action_markMenu:
                return true;


            default:
                return super.onOptionsItemSelected(item);

        }
    }
}