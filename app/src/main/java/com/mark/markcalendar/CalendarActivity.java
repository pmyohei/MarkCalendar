package com.mark.markcalendar;

import androidx.appcompat.app.ActionBar;
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