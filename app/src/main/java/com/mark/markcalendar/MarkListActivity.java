package com.mark.markcalendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/*
 * マーク一覧画面
 */
public class MarkListActivity extends AppCompatActivity {

    /*-- 定数 --*/
    /* 画面遷移-リクエストコード */
    public static final int REQ_CODE_CREATE = 100;
    public static final int REQ_CODE_EDIT = 101;

    /* 画面遷移-キー */
    public static String KEY_IS_CREATE = "isCreate";
    public static String KEY_MARK      = "Mark";

    //マークリスト
    //MarkArrayList<MarkTable> mMarks;
    //マークリストアダプタ
    //MarkListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_list);

        //ステータスバーの設定
        int color = getResources().getColor(R.color.primary);
        getWindow().setStatusBarColor(color);

        //ツールバー設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle( getResources().getString( R.string.title_mark_list ) );
        setSupportActionBar(toolbar);
        //戻るボタンの表示設定
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //共通データからマークリストを取得
        CommonData commonData = (CommonData)getApplication();
        MarkArrayList<MarkTable> marks = commonData.getMarks();

        //マーク新規作成・編集画面遷移ランチャー
        //※クリックリスナー内で定義しないこと！（ライフサイクルの関係でエラーになるため）
        ActivityResultLauncher<Intent> markEntryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    /*
                     * 画面遷移先からの戻り処理
                     */
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        Log.i("MarkListActivity", "onActivityResult()");

                        //インテント
                        Intent intent = result.getData();
                        //リザルトコード
                        int resultCode = result.getResultCode();

                        //新規作成結果
                        if(resultCode == MarkEntryActivity.RESULT_CREATED) {
                            //生成マーク
                            MarkTable mark = (MarkTable) intent.getSerializableExtra(MarkEntryActivity.KEY_MARK);
                            Log.i("MarkListActivity", "新規生成 mark=" + mark.getName() + " 色=" + mark.getColor());

                            //リストに追加し、アダプタに追加を通知
                            marks.add( mark );
                            ((RecyclerView)findViewById(R.id.rv_markList)).getAdapter().notifyItemInserted(marks.size() - 1);

                        //編集結果
                        } else if( resultCode == MarkEntryActivity.RESULT_EDITED) {
                            //編集後のマーク
                            MarkTable mark = (MarkTable) intent.getSerializableExtra(MarkListActivity.KEY_MARK);

                            //リスト内のマークを更新
                            int idx = marks.editMark( mark );
                            //アダプタに変更を通知
                            ((RecyclerView)findViewById(R.id.rv_markList)).getAdapter().notifyItemChanged( idx );

                        //その他
                        } else {
                            //do nothing
                        }
                    }
                }
        );


        //マークリストを表示
        RecyclerView rv_markList = findViewById(R.id.rv_markList);
        MarkListAdapter adapter = new MarkListAdapter((MarkArrayList<MarkTable>) marks, markEntryLauncher);
        rv_markList.setAdapter(adapter);
        rv_markList.setLayoutManager( new LinearLayoutManager(rv_markList.getContext()) );

        //ソート可能にする
        attachItemTouchHelper();

/*        //DB読み込み処理
        AsyncReadMark db = new AsyncReadMark(this, new AsyncReadMark.onFinishListener() {

            @Override
            public void onFinish(MarkArrayList<MarkTable> marks) {

                //DBから読み込んだマークをリストとして保持
                mMarks = marks;

                //レイアウトからリストビューを取得
                RecyclerView rv_markList = findViewById(R.id.rv_markList);
                //アダプタの生成
                mAdapter = new MarkListAdapter((ArrayList<MarkTable>) mMarks);
                //アダプタの設定
                rv_markList.setAdapter(mAdapter);
                //レイアウトマネージャの設定
                rv_markList.setLayoutManager( new LinearLayoutManager(rv_markList.getContext()) );

                //ソート可能にする
                attachItemTouchHelper();
            }
        });
        //非同期処理開始
        db.execute();*/


        //マーク新規作成ボタンリスナー
        findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent生成
                Intent intent = new Intent( MarkListActivity.this, MarkEntryActivity.class );
                //渡すデータ
                intent.putExtra(KEY_IS_CREATE, true);

                //画面遷移開始
                markEntryLauncher.launch( intent );
            }
        });
    }

    /*
     * マークリスト並び替え設定
     */
    private void attachItemTouchHelper(){

        //マークリスト
        RecyclerView rv_markList = findViewById(R.id.rv_markList);

        //共通データ
        CommonData commonData = (CommonData)getApplication();
        MarkArrayList<MarkTable> marks = commonData.getMarks();

        //ドラッグアンドドロップ、スワイプの設定(リサイクラービュー)
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.ACTION_STATE_IDLE) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {

                //移動位置取得
                final int fromPos = viewHolder.getAdapterPosition();
                final int toPos   = target.getAdapterPosition();

                //log-----
                Log.i("attachMarkListSort", "fromPos=" + fromPos + " toPos=" + toPos);
                for( MarkTable mark: marks ){
                    Log.i("attachMarkListSort", "before mark=" + mark.getName());
                }
                //log-----

                //マークリストの順番を変更
                Collections.swap(marks, fromPos, toPos);
                //アダプタへ通知
                rv_markList.getAdapter().notifyItemMoved(fromPos, toPos);

                //log-----
                for( MarkTable mark: marks ){
                    Log.i("attachMarkListSort", "after  mark=" + mark.getName());
                }
                //log-----

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            }

            /*
             * 最終的な処理終了時にコールされる
             */
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void clearView(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder) {

                //並び順を文字列として生成
                String order = marks.getOrder();

                //並び順を保存
                SharedPreferences spData = getSharedPreferences( CalendarActivity.SHARED_DATA_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = spData.edit();
                editor.putString( CalendarActivity.SHARED_KEY_MARK_ORDER, order);
                editor.apply();
            }
        });

        //マークリストをアタッチ
        helper.attachToRecyclerView(rv_markList);
    }

    /*
     * ツールバー 戻るボタン押下処理
     */
    @Override
    public boolean onSupportNavigateUp() {
        //アクティビティ終了
        finish();

        return super.onSupportNavigateUp();
    }
}