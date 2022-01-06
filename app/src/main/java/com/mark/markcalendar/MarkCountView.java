package com.mark.markcalendar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MarkCountView extends LinearLayout {

    //カウントアップ／ダウン
    public static final int COUNT_UP = 1;
    public static final int COUNT_DOWN = -1;

    //スライド方向
    public static final int LEFT  = 0;                        //左へスライド
    public static final int RIGTH = 1;                        //右へスライド

    /*
     *  コンストラクタ
     * 　 レイアウトに埋め込んだビューの生成時は、本コンストラクタがコールされる
     */
    public MarkCountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //レイアウト生成
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.mark_count_area, this, true);
    }

    /*
     * 表示するマーク数の初期化
     */
    public void initMarkArea() {
        ((TextView)findViewById(R.id.tv_markNumTotal)).setText("0");
        ((TextView)findViewById(R.id.tv_markNumMonth)).setText("0");
    }

    /*
     * 表示中のマーク数の設定
     */
    public void setupMarkNum( int markPid, String yearMonth, MarkDateArrayList<MarkDateTable> allMarkDates, boolean animation ) {
        //共通データから一時日付マーク情報を取得
        CommonData commonData = (CommonData)((Activity)getContext()).getApplication();
        TapDataArrayList<TapData> tapData = commonData.getTapData();

        //選択中マークの合計
        MarkDateArrayList<MarkDateTable> selectedMarkDates = allMarkDates.extractDesignatedMark(markPid);
        int totalNum = selectedMarkDates.size();
        //現時点のタップ情報を反映
        totalNum += tapData.getMarkedDateNum( markPid );

        //指定月のマーク数
        int monthNum = selectedMarkDates.getMonthNum( yearMonth );
        //現時点のタップ情報を反映
        monthNum += tapData.getMarkedDateNum( markPid, yearMonth );

        //アニメーション指定がなければ、ビューに値を設定して終了
        if( !animation ){
            //ビューに反映
            TextView tv_markNumTotal = findViewById(R.id.tv_markNumTotal);
            TextView tv_markNumMonth = findViewById(R.id.tv_markNumMonth);

            tv_markNumTotal.setText( Integer.toString( totalNum ) );
            tv_markNumMonth.setText( Integer.toString( monthNum ) );

            return;
        }

    }


    /*
     * 表示中の月のマーク数の変更
     */
    public void slideChangeMarkNumInMonth( int markPid, String yearMonth, MarkDateArrayList<MarkDateTable> allMarkDates, int direction ) {
        //共通データから一時日付マーク情報を取得
        CommonData commonData = (CommonData)((Activity)getContext()).getApplication();
        TapDataArrayList<TapData> tapData = commonData.getTapData();

        //選択中マークの合計
        MarkDateArrayList<MarkDateTable> selectedMarkDates = allMarkDates.extractDesignatedMark(markPid);

        //指定月のマーク数＋現時点のタップ情報
        int monthNum = selectedMarkDates.getMonthNum( yearMonth )
                     + tapData.getMarkedDateNum( markPid, yearMonth );

        //フェードアウト／フェードインアニメーション
        int out;
        int in;

        if( direction == LEFT ){
            out = R.anim.fade_out_left;
            in  = R.anim.fade_in_left;
        } else {
            out = R.anim.fade_out_right;
            in  = R.anim.fade_in_right;
        }

        //月のマーク数
        TextView tv_markNumMonth = findViewById(R.id.tv_markNumMonth);

        //アニメーション開始
        Animation outAnim = AnimationUtils.loadAnimation(getContext(), out);;
        tv_markNumMonth.startAnimation(outAnim);

        outAnim.setAnimationListener( new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {

                //ビューに反映
                tv_markNumMonth.setText( Integer.toString( monthNum ) );

                //アニメーション
                Animation inAnim = AnimationUtils.loadAnimation(getContext(), in);
                //アニメーション開始
                tv_markNumMonth.startAnimation(inAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationStart(Animation animation) { }
        });

    }


    /*
     * 表示中のマーク数のカウント変更
     */
    public void countUpDown( int value ) {
        //表示中のマーク数
        TextView tv_markNumTotal = findViewById(R.id.tv_markNumTotal);
        TextView tv_markNumMonth = findViewById(R.id.tv_markNumMonth);
        String totalStr = tv_markNumTotal.getText().toString();
        String monthStr = tv_markNumMonth.getText().toString();

        Log.i("countUpDown", "totalStr→" + totalStr + " monthStr=" + monthStr);

        //数に反映
        int totalNum = Integer.parseInt( totalStr ) + value;
        int monthNum = Integer.parseInt( monthStr ) + value;

        //フェードアウト／フェードインアニメーション
        int out;
        int in;

        if( value == COUNT_UP ){
            out = R.anim.fade_out_up;
            in  = R.anim.fade_in_up;
        } else {
            out = R.anim.fade_out_down;
            in  = R.anim.fade_in_down;
        }

        Animation outAnim = AnimationUtils.loadAnimation(getContext(), out);;
        //アニメーション開始
        tv_markNumTotal.startAnimation(outAnim);
        tv_markNumMonth.startAnimation(outAnim);

        outAnim.setAnimationListener( new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {

                //ビューに反映
                tv_markNumTotal.setText( Integer.toString( totalNum ) );
                tv_markNumMonth.setText( Integer.toString( monthNum ) );

                //アニメーション
                Animation inAnim = AnimationUtils.loadAnimation(getContext(), in);
                //アニメーション開始
                tv_markNumTotal.startAnimation(inAnim);
                tv_markNumMonth.startAnimation(inAnim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
            @Override
            public void onAnimationStart(Animation animation) { }
        });

    }


}
