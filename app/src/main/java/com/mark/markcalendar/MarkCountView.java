package com.mark.markcalendar;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MarkCountView extends LinearLayout {

    //カウントアップ／ダウン
    public static final int COUNT_UP   = 1;
    public static final int COUNT_DOWN = -1;

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
     * 表示中のマーク数の設定
     */
    public void setupMarkNum( int markPid, String yearMonth, MarkDateArrayList<MarkDateTable> allMarkDates ) {
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

        //ビューに反映
        TextView tv_markNumTotal = findViewById(R.id.tv_markNumTotal);
        TextView tv_markNumMonth = findViewById(R.id.tv_markNumMonth);

        tv_markNumTotal.setText( Integer.toString( totalNum ) );
        tv_markNumMonth.setText( Integer.toString( monthNum ) );
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

        //ビューに反映
        tv_markNumTotal.setText( Integer.toString( totalNum ) );
        tv_markNumMonth.setText( Integer.toString( monthNum ) );
    }


}
