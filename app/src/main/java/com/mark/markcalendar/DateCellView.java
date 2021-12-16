package com.mark.markcalendar;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.Serializable;

public class DateCellView extends LinearLayout {


    /*
     *  コンストラクタ
     */
    public DateCellView(Context context) {
        super(context);

        //レイアウト生成
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.calendar_day_cell, this, true);

        //レイアウト内のビューの設定
        setView();
    }

    /*
     * ビューの設定
     */
    private void setView(){

        //レイアウト全体にタッチリスナーを設定
        findViewById( R.id.ll_cell ).setClickable(true);    //！これがないとダブルタップが検知されない
        findViewById( R.id.ll_cell ).setOnTouchListener(new RootNodeTouchListener());
    }


    /*
     * ノードタッチリスナー
     */
    public class RootNodeTouchListener implements View.OnTouchListener {

        //ダブルタップ検知用
        public GestureDetector mDoubleTapDetector;

        /*
         * コンストラクタ
         */
        public RootNodeTouchListener() {
            //ダブルタップリスナーを実装したGestureDetector
            mDoubleTapDetector = new GestureDetector(getContext(), new DoubleTapListener());
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            //ダブルタップ処理
            return mDoubleTapDetector.onTouchEvent(event);
        }

        /*
         * ダブルタップリスナー
         */
        private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {

            /*
             * ダブルタップリスナー
             *　　マークの付与・削除を行う
             */
            @Override
            public boolean onDoubleTap(MotionEvent event) {

                Log.i("tap", "onDoubleTap");

                //日付の設定がない場合は、マーク設定対象外
                if( ((TextView)findViewById( R.id.tv_date)).getText().toString().isEmpty() ){
                    return true;
                }

                //マークビュー
                MarkView markView = findViewById( R.id.v_mark );

                //マークの付与・削除
                if( markView.getVisibility() == View.INVISIBLE ){
                    markView.setVisibility( View.VISIBLE );
                } else {
                    markView.setVisibility( View.INVISIBLE );
                }

                return true;
            }
        }
    }



}
