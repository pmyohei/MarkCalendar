package com.mark.markcalendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

/*
 * カレンダーActivity
 */
public class CalendarActivity extends AppCompatActivity {

    /*-- 定数 --*/
    /* 画面遷移-リクエストコード */
    //マーク一覧画面へ
    public static final int REQ_MARK_INFORMATION = 100;

    /* 画面遷移-キー */
    public static String INTENT_MARK_PID = "MarkPid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //ステータスバーの設定
        int color = getResources().getColor(R.color.primary);
        getWindow().setStatusBarColor(color);

        //ツールバー設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("選択中マークを表示");
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);


/*
        actionbar.setDisplayShowTitleEnabled(false);
        actionbar.setNavigationMode(
                ActionBar.NAVIGATION_MODE_LIST
        );
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this,
                        R.array.planets_array,
                        R.layout.actionbar_spinner);
        adapter.setDropDownViewResource(
                R.layout.actionbar_spinner_dropdown
        );
        actionbar.setListNavigationCallbacks(adapter, this);
*/

        //カレンダーの表示
        GridView calendarGridView = findViewById(R.id.calendarGridView);
        CalendarAdapter mCalendarAdapter = new CalendarAdapter(this);
        calendarGridView.setAdapter(mCalendarAdapter);

        //表示中の年月ビュー
        TextView tv_yearMonth = findViewById(R.id.tv_yearMonth);

        //前月に変更
        findViewById(R.id.ib_preMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //カレンダーを前月に変更
                mCalendarAdapter.prevMonth();

                //表示年月の変更
                tv_yearMonth.setText(mCalendarAdapter.getMonth());
            }
        });
        //次月に変更
        findViewById(R.id.ib_nextMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //カレンダーを前月に変更
                mCalendarAdapter.nextMonth();

                //表示年月の変更
                tv_yearMonth.setText(mCalendarAdapter.getMonth());
            }
        });


    }

    /*
     * ツールバーオプションメニュー生成
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_calendar, menu);

        return true;
    }

    /*
     * ツールバーアクション選択
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //選択中マーク変更
            case R.id.action_markPullDown:

                //プルダウン表示

                return true;

            //マーク一覧画面へ
            case R.id.action_markMenu:
                //画面遷移
                Intent intent = new Intent(this, MarkInformationActivity.class);
                //選択中マーク情報を渡す
                intent.putExtra(INTENT_MARK_PID, 0);
                //画面遷移
                ((Activity)this).startActivityForResult(intent, REQ_MARK_INFORMATION);

                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}