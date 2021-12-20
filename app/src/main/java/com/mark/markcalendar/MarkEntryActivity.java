package com.mark.markcalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

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

        //遷移元からの情報
        Intent intent = getIntent();
        boolean isCreate = intent.getBooleanExtra( MarkListActivity.KEY_IS_CREATE, false );

        if( isCreate ){
            //OKボタンリスナー
            findViewById(R.id.bt_positive).setOnClickListener( new PositiveClickListener(null) );

        } else {
            //編集対象のマークを取得
            MarkTable mark = (MarkTable) intent.getSerializableExtra(MarkListActivity.KEY_MARK);

            //登録済み情報として設定
            EditText et_markName = findViewById( R.id.et_markName );
            et_markName.setText( mark.getName() );

            MarkView markView = findViewById( R.id.v_mark );
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

        //

    }

    /*
     * 色選択エリアの設定
     */
    private void setSelectColor(){
        //色選択エリア
        LinearLayout ll_colorSelectArea = findViewById(R.id.ll_colorSelectArea);
        //選択マーク
        MarkView v_mark = findViewById(R.id.v_mark);

        //選択エリアビューの直下のビューを取得
        int parentNum = ll_colorSelectArea.getChildCount();
        for( int i = 0; i < parentNum; i++ ){

            View v_parent = ll_colorSelectArea.getChildAt(i);
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
                                v_mark.setColorHex(color);
                            }
                        });
                    }
                }
            }
        }

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
            int    markColor = ((MarkView)findViewById( R.id.v_mark )).getColorHex();

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

                    //resultコード設定
                    intent.putExtra(KEY_MARK, mark);
                    setResult(RESULT_CREATED, intent);

                    Log.i("Map", "マーク新規生成完了。リスト画面へ戻る");

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

                    //resultコード設定
                    intent.putExtra(KEY_MARK, mark);
                    setResult(RESULT_EDITED, intent);

                    Log.i("Map", "マーク編集完了。リスト画面へ戻る");

                    //元の画面へ戻る
                    finish();
                }
            });

            //非同期処理開始
            db.execute();
        }

    }





}