package com.mark.markcalendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
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

    //フリック検知
    private GestureDetector mFlingDetector;
    //カレンダーアダプタ
    CalendarAdapter mCalendarAdapter;
    //画面遷移ランチャー
    ActivityResultLauncher<Intent> mMarkListLauncher;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //マークリスト画面遷移ランチャー
        //※クリックリスナー内で定義しないこと！（ライフサイクルの関係でエラーになるため）
        mMarkListLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    /*
                     * 画面遷移先からの戻り処理
                     */
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        Log.i("CalendarActivity", "画面リストからの戻り");

                        //インテント
                        Intent intent = result.getData();
                        //リザルトコード
                        int resultCode = result.getResultCode();

                        //新規作成結果
                        if(resultCode == MarkEntryActivity.RESULT_CREATED) {

                            //Log.i("MarkInformationActivity", "新規生成 mark=" + mark.getName() + " 色=" + mark.getColor());

                        //編集結果
                        } else if( resultCode == MarkEntryActivity.RESULT_EDITED) {

                        //その他
                        } else {
                            //do nothing
                        }
                    }
                }
        );



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
        GridView calendarGridView = findViewById(R.id.gv_calendar);
        mCalendarAdapter = new CalendarAdapter(this);
        calendarGridView.setAdapter(mCalendarAdapter);

        //アプリ起動時点の年月を表示
        TextView tv_yearMonth = findViewById(R.id.tv_yearMonth);
        tv_yearMonth.setText(mCalendarAdapter.getMonth());

        //前月に変更
        findViewById(R.id.ib_preMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //カレンダーを前月に変更
                previousMonth();
            }
        });
        //次月に変更
        findViewById(R.id.ib_nextMonth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //カレンダーを次月に変更
                nextMonth();
            }
        });

        //フリック検知リスナー
        mFlingDetector = new GestureDetector(this, new FlingListener());

        //マーク表示エリア
        LinearLayout ll_markEria = findViewById(R.id.ll_markEria);
        ll_markEria.setClickable(true);
        ll_markEria.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Log.i("onTouch", "onTouch");

                //フリング検知に渡す
                mFlingDetector.onTouchEvent(motionEvent);

                return false;
            }
        });

    }

    /*
     * 次月を表示
     */
    private void nextMonth(){
        //カレンダーを次月に変更
        mCalendarAdapter.nextMonth();

        //表示年月の変更
        TextView tv_yearMonth = findViewById(R.id.tv_yearMonth);
        tv_yearMonth.setText(mCalendarAdapter.getMonth());
    }

    /*
     * 前月を表示
     */
    private void previousMonth(){
        //カレンダーを前月に変更
        mCalendarAdapter.prevMonth();

        //表示年月の変更
        TextView tv_yearMonth = findViewById(R.id.tv_yearMonth);
        tv_yearMonth.setText(mCalendarAdapter.getMonth());
    }

    /*
     * 画面遷移-マークリスト画面
     */
    private void transitionMarkList(){
        //画面遷移
        Intent intent = new Intent(this, MarkListActivity.class);
        //選択中マーク情報を渡す
        intent.putExtra(INTENT_MARK_PID, 0);
        //画面遷移
        mMarkListLauncher.launch( intent );
    }

    /*
     * スワイプ操作リスナー\
     *   ・フリング
     */
    private class FlingListener extends GestureDetector.SimpleOnGestureListener {

        //フリング検知対象とする移動量の閾値
        private final int THRESHOLD_FLING_DETECTION = 100;

        //フリング方向
        private final int FLING_NONE  = -1;
        private final int FLING_RIGHT = 0;
        private final int FLING_LEFT  = 1;
        private final int FLING_UP    = 2;
        private final int FLING_DOWN  = 3;

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("onFling", "onDown e1 x=" + e.getX() + "e1 y=" + e.getY());
            return true;
        }

        /*
         * ダブルタップリスナー
         */
        @Override
        public boolean onDoubleTap(MotionEvent event) {

            Log.i("tap", "onDoubleTap");

            //マークリスト画面を開く
            transitionMarkList();

            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.i("onFling", "e1 x=" + e1.getX() + " e1 y=" + e1.getY() + " e2 x=" + e2.getX() + " e2 y=" + e2.getY());
            Log.i("onFling", "velocityX=" + velocityX + " velocityY=" + velocityY);

            //移動量
            int distanceX = (int)(e1.getX() - e2.getX());
            int distanceY = (int)(e1.getY() - e2.getY());
            //移動量（絶対値）
            int distanceAbsX = Math.abs( distanceX );
            int distanceAbsY = Math.abs( distanceY );

            //方向検出
            int direction = detectFlingDirection(distanceAbsX, distanceAbsY, distanceX, distanceY);

            switch (direction){

                case FLING_LEFT:
                    Log.i("onFling", "FLING_LEFT");
                    nextMonth();
                    break;

                case FLING_RIGHT:
                    Log.i("onFling", "FLING_RIGHT");
                    previousMonth();
                    break;

                case FLING_UP:
                    Log.i("onFling", "FLING_UP");
                    break;

                case FLING_DOWN:
                    Log.i("onFling", "FLING_DOWN");
                    break;

                default:
                    break;
            }

            Log.i("onFling", "-----------------");

            return false;
        }

        /*
         * フリング方向の検出
         *   ※XY軸がどちらも検出閾値を超過している場合は、「フリングなし」とする。
         *   ※XY軸がどちらも検出閾値を超過していない場合は、「フリングなし」とする。
         */
        public int detectFlingDirection(int distanceAbsX, int distanceAbsY, int distanceX, int distanceY){

            //フリングなし
            int direction = FLING_NONE;

            //どちらも閾値未満 or どちらも閾値超過
            if( ( (distanceAbsX <= THRESHOLD_FLING_DETECTION) && (distanceAbsY <= THRESHOLD_FLING_DETECTION) )
                    || ( (distanceAbsX > THRESHOLD_FLING_DETECTION) && (distanceAbsY > THRESHOLD_FLING_DETECTION) ) ){
                return direction;
            }

            if( distanceAbsX > THRESHOLD_FLING_DETECTION ){
                //横方向のフリング発生

                if( distanceX > 0 ){
                    //左にフリングが発生
                    direction = FLING_LEFT;
                } else {
                    //右にフリングが発生
                    direction = FLING_RIGHT;
                }

            } else {
                //縦方向のフリング発生

                if( distanceY > 0 ){
                    //上にフリングが発生
                    direction = FLING_UP;
                } else {
                    //下にフリングが発生
                    direction = FLING_DOWN;
                }
            }

            return direction;
        }

    }

/*    public boolean onTouchEvent(MotionEvent motionEvent) {
        Log.i("onTouchEvent", "parent onTouch");

        return false;
    }*/

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
                transitionMarkList();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }





}