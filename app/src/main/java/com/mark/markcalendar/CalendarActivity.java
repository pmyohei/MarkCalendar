package com.mark.markcalendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * カレンダーActivity
 */
public class CalendarActivity extends AppCompatActivity {

    /*-- 定数 --*/
    //画面遷移-キー
    public static String INTENT_MARK_PID = "MarkPid";

    //SharedPreferences
    public static final String SHARED_DATA_NAME = "UIData";         //SharedPreferences保存名
    private final String SHARED_KEY_SELECTED_MARK = "SelectedMark"; //選択中マーク
    public static final String SHARED_KEY_MARK_ORDER = "MarkOrder"; //マークの並び順
    public static final int INVALID_SELECTED_MARK = -1;             //選択中マーク（取得エラー時）
    public static final String INVALID_MARK_ORDER = "";             //マークの並び順（取得エラー時）

    //フリック検知
    private GestureDetector mFlingDetector;
    //カレンダーアダプタ
    private CalendarAdapter mCalendarAdapter;
    //画面遷移ランチャー
    private ActivityResultLauncher<Intent> mMarkListLauncher;
    //日付マークリスト(全データ)
    private MarkDateArrayList<MarkDateTable> mAllMarkDates;
    //選択中マーク
    //★pidにするかも（最後に見直し）
    private MarkTable mSelectedMark;

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

                        Log.i("CalendarActivity", "マークリストからの戻り");

                        //選択中マークの更新が必要かチェック
                        checkUpdateSelectedMark();
                    }
                }
        );


        //DB読み込み処理
        AsyncReadMark db = new AsyncReadMark(this, new AsyncReadMark.onFinishListener() {

            @Override
            public void onFinish(MarkArrayList<MarkTable> marks, MarkDateArrayList<MarkDateTable> markDates) {

                //全日付マークを保持
                mAllMarkDates = markDates;

                //マーク並び順を取得
                SharedPreferences spData = getSharedPreferences(SHARED_DATA_NAME, MODE_PRIVATE);
                String order = spData.getString(SHARED_KEY_MARK_ORDER, INVALID_MARK_ORDER);

                //DBから読み込んだマークを共通データとして保持
                CommonData commonData = (CommonData) getApplication();
                MarkArrayList<MarkTable> marksInOrder = commonData.createMarksInOrder(marks, order);

                //マーク数表示ビュー
                MarkCountView mv_markEria = findViewById(R.id.mv_markEria);

                //カレンダーの表示
                GridView calendarGridView = findViewById(R.id.gv_calendar);
                mCalendarAdapter = new CalendarAdapter(calendarGridView.getContext(), markDates, mv_markEria);
                calendarGridView.setAdapter(mCalendarAdapter);

                //アプリ起動時点の年月を表示
                TextView tv_yearMonth = findViewById(R.id.tv_yearMonth);
                tv_yearMonth.setText(mCalendarAdapter.getMonth());

                if (marks.size() == 0) {
                    return;
                }

                //ユーザーの前回選択中マークを取得
                MarkTable mark = getUserSelectedMark(marksInOrder);

                //選択中マーク設定
                setSelectedMark(mark, MarkCountView.NONE);
            }
        });
        //非同期処理開始
        db.execute();

        //ステータスバーの設定
        int color = getResources().getColor(R.color.primary);
        getWindow().setStatusBarColor(color);

        //ツールバー設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("選択中マークを表示");
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(false);

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
     * ユーザーが前回アプリ使用時に選択していたマークを取得
     */
    private MarkTable getUserSelectedMark(MarkArrayList<MarkTable> marksInOrder) {

        //前回の選択中マークを取得
        SharedPreferences spData = getSharedPreferences(SHARED_DATA_NAME, MODE_PRIVATE);
        int selectedMarkPid = spData.getInt(SHARED_KEY_SELECTED_MARK, INVALID_SELECTED_MARK);

        //マーク
        MarkTable mark;

        //前回情報なし
        if (selectedMarkPid == INVALID_SELECTED_MARK) {
            //先頭のマークを選択中とする
            mark = marksInOrder.get(0);

        } else {
            //選択中マークを取得
            mark = marksInOrder.getMark(selectedMarkPid);
            if (mark == null) {
                //Failsafe
                //もしなければ、先頭マークにする
                mark = marksInOrder.get(0);
            }
        }

        return mark;
    }

    /*
     * 次月を表示
     */
    private void nextMonth() {
        //カレンダーを次月に変更
        mCalendarAdapter.nextMonth();

        //マーク数表示エリアの更新
        setupMonthMarkNum( MarkCountView.LEFT );

        //表示年月の変更
        TextView tv_yearMonth = findViewById(R.id.tv_yearMonth);
        tv_yearMonth.setText(mCalendarAdapter.getMonth());
    }

    /*
     * 前月を表示
     */
    private void previousMonth() {
        //カレンダーを前月に変更
        mCalendarAdapter.prevMonth();

        //マーク数表示エリアの更新
        setupMonthMarkNum( MarkCountView.RIGHT);

        //表示年月の変更
        TextView tv_yearMonth = findViewById(R.id.tv_yearMonth);
        tv_yearMonth.setText(mCalendarAdapter.getMonth());
    }

    /*
     * 次のマークに変更
     */
    private void nextMark() {

        //共通データからマークリストを取得
        CommonData commonData = (CommonData) getApplication();
        MarkArrayList<MarkTable> marks = commonData.getMarks();

        //マークがなし or マーク1つ なら何もしない
        if (marks.size() <= 1) {
            return;
        }

        //選択中マークの変更
        MarkTable mark = marks.getNextMark(mSelectedMark.getPid());
        setSelectedMark(mark, MarkCountView.UP);
    }

    /*
     * 前のマークに変更
     */
    private void previousMark() {

        //共通データからマークリストを取得
        CommonData commonData = (CommonData) getApplication();
        MarkArrayList<MarkTable> marks = commonData.getMarks();

        //マークがなし or マーク1つ なら何もしない
        if (marks.size() <= 1) {
            return;
        }

        //選択中マークの変更
        MarkTable mark = marks.getPreviousMark(mSelectedMark.getPid());
        setSelectedMark(mark, MarkCountView.DOWN);
    }

    /*
     * 選択中マークの変更
     */
    private void setSelectedMark(MarkTable mark, int direction) {

        //選択中マークを更新
        mSelectedMark = mark;

        //選択中マークビュー
        TextView tv_selectedMark = findViewById(R.id.tv_selectedMark);

        //★保存用クラスを作成して、それに任せた方がよいかも→現状、複数個所で保存処理あり
        SharedPreferences spData = getSharedPreferences(SHARED_DATA_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = spData.edit();

        if (mark == null) {
            //マーク未登録文字列を設定
            String noMarkText = getResources().getString(R.string.no_mark);
            tv_selectedMark.setText(noMarkText);

            //無効値を設定
            editor.putInt(SHARED_KEY_SELECTED_MARK, INVALID_SELECTED_MARK);
        } else {

            //フェードアウトアニメーション
            int out = ( (direction == MarkCountView.UP) ? R.anim.fade_out_up : R.anim.fade_out_down);
            Animation outAnim = AnimationUtils.loadAnimation(this, out);;

            //アニメーション開始
            tv_selectedMark.startAnimation(outAnim);
            //アニメーションリスナーの設定
            outAnim.setAnimationListener( new FadeUpDownAnimationListener( tv_selectedMark, mSelectedMark.getName(), direction ) );

            //指定マークを設定
            editor.putInt(SHARED_KEY_SELECTED_MARK, mSelectedMark.getPid());
        }

        //カレンダーのマーク情報を変更
        mCalendarAdapter.setMark(mSelectedMark);

        //選択中マークを保存
        //★保存用クラスを作成して、それに任せた方がよいかも→現状、複数個所で保存処理あり
        editor.apply();

        //表示するマーク数を設定
        setupMarkArea( direction, true );
    }

    /*
     * 画面遷移-マークリスト画面
     */
    private void transitionMarkList() {
        //画面遷移
        Intent intent = new Intent(this, MarkListActivity.class);
        //選択中マーク情報を渡す
        intent.putExtra(INTENT_MARK_PID, 0);
        //画面遷移
        mMarkListLauncher.launch(intent);
    }

    /*
     * 選択中マークの更新チェック
     */
    private void checkUpdateSelectedMark() {

        //マークリストを取得
        CommonData commonData = (CommonData) getApplication();
        MarkArrayList<MarkTable> marks = commonData.getMarks();

        //表示中の選択中マークビュー
        TextView tv_selectedMark = findViewById(R.id.tv_selectedMark);

        //マーク未登録文字列
        String noMarkText = getResources().getString(R.string.no_mark);

        //登録マークなしの状態
        if (tv_selectedMark.getText().toString().equals(noMarkText)) {

            //マークが登録されれば
            if (marks.size() > 0) {
                //先頭のマークを選択中マークとする
                MarkTable mark = marks.get(0);
                //選択中マークの設定
                setSelectedMark(mark, MarkCountView.NONE);
            }

        } else {

            //マークが全て削除されたなら
            if (marks.size() == 0) {
                //選択中マークなし
                setSelectedMark(null, MarkCountView.NONE);

            } else {
                //マークはすべて削除されていない場合

                //選択中マークが削除されている場合
                if (marks.getMark(mSelectedMark.getPid()) == null) {
                    //先頭のマークを選択中に変更する
                    setSelectedMark(marks.get(0), MarkCountView.NONE);

                } else {
                    //削除されていなくとも、編集されている場合を考慮し、選択中マークの表示を更新
                    tv_selectedMark.setText(mSelectedMark.getName());
                }
            }
        }

    }

    /*
     * マーク数表示エリアの表示
     */
    private void setupMarkArea( int direction, boolean animation ) {

        //マーク数表示エリアビュー
        MarkCountView mv_markEria = findViewById(R.id.mv_markEria);

        //選択中マークがあるか
        if( mSelectedMark == null ){
            mv_markEria.initMarkArea();
        } else{
            mv_markEria.setMarkNum(mSelectedMark.getPid(), mCalendarAdapter.getMonth(), mAllMarkDates, animation, direction);
        }
    }

    /*
     * マーク数表示エリアの月のマーク数の表示
     */
    private void setupMonthMarkNum( int direction ) {

        if( mSelectedMark != null ){
            //マーク数表示エリアビュー
            MarkCountView mv_markEria = findViewById(R.id.mv_markEria);
            //月のマーク数のみを変更
            mv_markEria.slideChangeMarkNumInMonth(mSelectedMark.getPid(), mCalendarAdapter.getMonth(), mAllMarkDates, direction);
        }
    }
    
    /*
     * スワイプ操作リスナー\
     *   ・フリング
     */
    private class FlingListener extends GestureDetector.SimpleOnGestureListener {

        //フリング検知対象とする移動量の閾値
        private final int THRESHOLD_FLING_DETECTION = 100;

        //フリング方向
        private final int FLING_NONE = -1;
        private final int FLING_RIGHT = 0;
        private final int FLING_LEFT = 1;
        private final int FLING_UP = 2;
        private final int FLING_DOWN = 3;

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
            int distanceX = (int) (e1.getX() - e2.getX());
            int distanceY = (int) (e1.getY() - e2.getY());
            //移動量（絶対値）
            int distanceAbsX = Math.abs(distanceX);
            int distanceAbsY = Math.abs(distanceY);

            //方向検出
            int direction = detectFlingDirection(distanceAbsX, distanceAbsY, distanceX, distanceY);

            switch (direction) {

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
                    nextMark();
                    break;

                case FLING_DOWN:
                    Log.i("onFling", "FLING_DOWN");
                    previousMark();
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
        public int detectFlingDirection(int distanceAbsX, int distanceAbsY, int distanceX, int distanceY) {

            //フリングなし
            int direction = FLING_NONE;

            //どちらも閾値未満 or どちらも閾値超過
            if (((distanceAbsX <= THRESHOLD_FLING_DETECTION) && (distanceAbsY <= THRESHOLD_FLING_DETECTION))
                    || ((distanceAbsX > THRESHOLD_FLING_DETECTION) && (distanceAbsY > THRESHOLD_FLING_DETECTION))) {
                return direction;
            }

            if (distanceAbsX > THRESHOLD_FLING_DETECTION) {
                //横方向のフリング発生

                if (distanceX > 0) {
                    //左にフリングが発生
                    direction = FLING_LEFT;
                } else {
                    //右にフリングが発生
                    direction = FLING_RIGHT;
                }

            } else {
                //縦方向のフリング発生

                if (distanceY > 0) {
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

    /*
     * ツールバーオプションメニュー生成
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //メニューをインフレート
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

                //マークリスト
                CommonData commonData = (CommonData) getApplication();
                MarkArrayList<MarkTable> marks = commonData.getMarks();

                //プルダウン表示
                MarkSpinnerDialog dialog = new MarkSpinnerDialog( marks );

                dialog.setOnItemClickListener( new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //選択マーク
                        MarkTable mark = marks.get(position);

                        //選択中のマークと異なれば、変更
                        if( mark.getPid() != mSelectedMark.getPid() ){
                            setSelectedMark( mark, MarkCountView.UP );
                        }
                    }
                });
                dialog.show( getSupportFragmentManager(), "mark");

                return true;

            //マーク一覧画面へ
            case R.id.action_markMenu:
                transitionMarkList();
                return true;

            //ヘルプボタン
            case R.id.action_help:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
     * onStop()
     */
    @Override
    protected void onStop() {
        //必須
        super.onStop();

        Log.i("lifecycle", "onStop");

        //DBから読み込んだマークを共通データとして保持
        CommonData commonData = (CommonData) getApplication();
        TapDataArrayList<TapData> tapData = commonData.getTapData();

        //DB更新
        AsyncUpdateMarkDate db = new AsyncUpdateMarkDate(this, tapData, new AsyncUpdateMarkDate.onFinishListener() {

            @Override
            public void onFinish() {

                //キュークリア
                tapData.clear();
            }
        });
        //非同期処理開始
        db.execute();
    }

    /*
     * onRestart()
     */
    @Override
    protected void onRestart() {
        //必須
        super.onRestart();

        Log.i("lifecycle", "onRestart");

        //DB読み込み処理
        //★マークリストは不要
        AsyncReadMark db = new AsyncReadMark(this, new AsyncReadMark.onFinishListener() {

            @Override
            public void onFinish(MarkArrayList<MarkTable> marks, MarkDateArrayList<MarkDateTable> markDates) {
                //アダプタの日付マーク情報を更新
                mCalendarAdapter.updateMarkDate( markDates );

                //マーク数エリアを更新
                setupMarkArea( MarkCountView.UP, false );
            }
        });
        //非同期処理開始
        db.execute();

    }
}