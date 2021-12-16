package com.mark.markcalendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MarkNumView_old extends LinearLayout {

    //フリック検知
    private final GestureDetector mFlingGestureDetector;

    /*
     *  コンストラクタ
     * 　 レイアウトに埋め込んだビューの生成時は、本コンストラクタがコールされる
     */
    public MarkNumView_old(Context context, AttributeSet attrs) {
        super(context, attrs);

        //レイアウト生成
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //inflater.inflate(R.layout.mark_num_area, this, true);
        View aaa = inflater.inflate(R.layout.mark_count_area, this);

        //クリックリスナー
        //※空のクリック処理をオーバーライドしないと、ジェスチャー処理が検出されないため、空処理を入れとく
        //※「implements View.OnClickListener」で空処理を入れるのはなぜか効果なし
/*        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
            }
        });*/
        //aaa.setClickable(true);
        //aaa.setLongClickable(true);

        //フリック検知リスナー
        mFlingGestureDetector = new GestureDetector(this.getContext(), new FlingListener());

    }

    /*
     * タッチイベントの実装
     */
/*    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        //フリング検知に渡す
        boolean ret = mFlingGestureDetector.onTouchEvent(motionEvent);

        Log.i("onTouchEvent", "ret=" + ret);

        return false;
    }*/

    /*
     * スワイプ操作リスナー\
     *   ・フリング
     */
    private static class FlingListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("onFling", "onDown");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            Log.i("onFling", "e1 x=" + e1.getX() + "e1 y=" + e1.getY() + "e2 x=" + e2.getX() + "e2 y=" + e2.getX());
            Log.i("onFling", "velocityX=" + velocityX + " velocityY=" + velocityY);
            Log.i("onFling", "-----------------");

            return false;
        }

    }
}
