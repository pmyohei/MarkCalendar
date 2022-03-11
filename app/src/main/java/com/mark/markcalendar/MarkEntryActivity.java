package com.mark.markcalendar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.Objects;

public class MarkEntryActivity extends AppCompatActivity {

    /*-- 定数 --*/
    /* 画面遷移-レスポンスコード */
    public static final int RESULT_CREATED = 100;
    public static final int RESULT_EDITED  = 101;

    /* 画面遷移-キー */
    public static String KEY_MARK = "mark";         //対象マーク

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_entry);

        //ステータスバーの設定
        int color = getResources().getColor(R.color.primary);
        getWindow().setStatusBarColor(color);

        //ツールバー設定
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle( "" );
        setSupportActionBar(toolbar);
        //戻るボタンの表示設定
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Admobロード
        AdView adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        //遷移元からの情報
        Intent intent = getIntent();
        boolean isCreate = intent.getBooleanExtra( MarkListActivity.KEY_IS_CREATE, false );

        //選択マーク
        MarkView markView = findViewById( R.id.mv_selectedMark );

        if( isCreate ){
            //選択マークは先頭の色にする
            markView.setColorHex( getResources().getColor( R.color.mark_1 ) );
            
            //OKボタンリスナー
            findViewById(R.id.bt_positive).setOnClickListener( new PositiveClickListener(null) );

        } else {
            //編集対象のマークを取得
            MarkTable mark = (MarkTable) intent.getSerializableExtra(MarkListActivity.KEY_MARK);

            //登録済み情報として設定
            EditText et_markName = findViewById( R.id.et_markName );
            et_markName.setText( mark.getName() );

            markView.setColorHex( mark.getColor() );

            //OKボタンリスナー
            findViewById(R.id.bt_positive).setOnClickListener( new PositiveClickListener(mark) );
        }

        //キャンセルボタン
        findViewById(R.id.bt_negative).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //resultコード設定
                intent.putExtra(KEY_MARK, 123 );
                setResult(RESULT_CANCELED, intent );

                //元の画面へ戻る
                finish();
            }
        });

        //色選択リストの設定
        setSelectColor();
    }

    /*
     * 色選択エリアの設定
     */
    private void setSelectColor() {
        //色選択エリア
        ViewGroup cl_colorSelectArea = findViewById(R.id.cl_colorSelectArea);
        //選択マーク
        MarkView mv_selectedMark = findViewById(R.id.mv_selectedMark);

        //選択エリアビューの直下のビューを取得
        int childNum = cl_colorSelectArea.getChildCount();
        for( int i = 0; i < childNum; i++ ){
            //子ビュー
            View selectMark = cl_colorSelectArea.getChildAt(i);

            //MarkViewに対して、クリックリスナーを設定
            if( selectMark instanceof MarkView ){
                selectMark.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //クリックマークの色を取得
                        int color = ((MarkView)view).getColorHex();
                        //選択中マークに色を反映
                        mv_selectedMark.setColorHex(color);
                    }
                });
            }
        }

/*        int parentNum = cl_colorSelectArea.getChildCount();
        for( int i = 0; i < parentNum; i++ ){

            View v_parent = cl_colorSelectArea.getChildAt(i);
            //親ビューの場合、その直下のビューを設定
            if( v_parent instanceof ViewGroup) {
                int colorNum = ((ViewGroup)v_parent).getChildCount();
                for (int j = 0; j < colorNum; j++) {
                    View selectMark = ((ViewGroup)v_parent).getChildAt(j);

                    //MarkViewに対して、クリックリスナーを設定
                    if( selectMark instanceof MarkView ){
                        selectMark.setOnClickListener( new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //クリックマークの色を取得
                                int color = ((MarkView)view).getColorHex();
                                //選択中マークに色を反映
                                mv_selectedMark.setColorHex(color);
                            }
                        });
                    }
                }
            }
        }*/

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


    /*
     *
     * OKボタンクリックリスナー
     *
     */
    private class PositiveClickListener implements View.OnClickListener {

        //対象マーク（新規生成の場合はnullが渡される）
        private MarkTable mMark;

        /*
         * コンストラクタ
         */
        public PositiveClickListener(MarkTable mark) {
            mMark = mark;
        }

        @Override
        public void onClick(View view) {

            //入力チェック
            if (verifyInputData()) {
                Toast.makeText(view.getContext(), R.string.toast_no_mark_name, Toast.LENGTH_SHORT).show();
                return;
            }

            //新規生成
            boolean isCreate = false;

            //新規生成なら、ここでマークを生成
            if( mMark == null){
                mMark = new MarkTable();

                isCreate = true;
            }

            //入力データを設定
            String markName  = ((EditText)findViewById( R.id.et_markName )).getText().toString();
            int    markColor = ((MarkView)findViewById( R.id.mv_selectedMark )).getColorHex();

            mMark.setName( markName );
            mMark.setColor( markColor );

            //非同期処理
            if( isCreate ){
                doAsyncCreate(view.getContext(), getIntent(), mMark);
            } else {
                doAsyncUpdate(view.getContext(), getIntent(), mMark);
            }
        }

        /*
         * 入力チェック
         */
        private boolean verifyInputData() {

            //マーク名
            EditText et_markName = findViewById(R.id.et_markName);
            String markName = et_markName.getText().toString();

            //空かどうか
            return markName.isEmpty();
        }

        /*
         * 非同期処理-新規生成
         */
        private void doAsyncCreate(Context context, Intent intent, MarkTable mark){
            //DB保存処理
            AsyncCreateMark db = new AsyncCreateMark(context, mark, new AsyncCreateMark.onFinishListener() {

                @Override
                public void onFinish(int pid) {
                    //データ挿入されたため、レコードに割り当てられたpidをノードに設定
                    mark.setPid(pid);

                    //共通データからマークリストを取得
                    CommonData commonData = (CommonData)getApplication();
                    MarkArrayList<MarkTable> marks = commonData.getMarks();
                    //マークをリストに追加
                    marks.add( mark );

                    //resultコード設定
                    setResult(RESULT_CREATED);

                    Log.i("Mark", "マーク新規生成完了。リスト画面へ戻る");

                    //元の画面へ戻る
                    finish();
                }
            });

            //非同期処理開始
            db.execute();
        }

        /*
         * 非同期処理-更新
         */
        private void doAsyncUpdate(Context context, Intent intent, MarkTable mark){
            //DB保存処理
            AsyncUpdateMark db = new AsyncUpdateMark(context, mark, new AsyncUpdateMark.onFinishListener() {

                @Override
                public void onFinish() {

                    //共通データからマークリストを取得
                    CommonData commonData = (CommonData)getApplication();
                    MarkArrayList<MarkTable> marks = commonData.getMarks();
                    //リスト内のマークを更新
                    int idx = marks.editMark( mark );

                    //resultコード設定
                    intent.putExtra(MarkListActivity.KEY_MARK, idx);
                    setResult(RESULT_EDITED, intent);

                    Log.i("Mark", "マーク編集完了。リスト画面へ戻る");

                    //元の画面へ戻る
                    finish();
                }
            });

            //非同期処理開始
            db.execute();
        }

    }





}