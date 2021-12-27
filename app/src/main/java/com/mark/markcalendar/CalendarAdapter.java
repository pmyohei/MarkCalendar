package com.mark.markcalendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {

    private Context         mContext;                       //コンテキスト
    private List<Date>      mDaysInMonth = new ArrayList(); //選択月の日リスト
    private DateManager     mDateManager;                   //カレンダー管理用
    private LayoutInflater  mLayoutInflater;                //描画高速化のために必要
    //public int              mMarkColor;                     //マーク色
    private MarkTable       mSelectedMark;

    /*
     * 日付レイアウトクラス
     */
    private class ViewHolder {
        public LinearLayout ll_cell;
        public TextView tv_date;
        public MarkView v_mark;

        private int position;
        private int markPid;

        /*
         * ビューの設定
         */
        @SuppressLint("ClickableViewAccessibility")
        public void setView(View view ){
            ll_cell = view.findViewById(R.id.ll_cell);
            tv_date = view.findViewById(R.id.tv_date);
            v_mark  = view.findViewById(R.id.v_mark);

            //レイアウト全体にタッチリスナーを設定
            //ll_cell.setClickable(true);                             //！これがないとダブルタップが検知されない
            //ll_cell.setOnTouchListener(new DateTouchListener( this ));
        }

        /*
         * 表示情報の初期化
         */
        public void clearData( int color, int position, int markPid ){

            //ビューの表示初期化
            tv_date.setText("");
            v_mark.setVisibility( View.INVISIBLE ); //★
            v_mark.setColorHex( color );

            //セル位置
            this.position = position;
            //マークPid
            this.markPid = markPid;

            //レイアウト全体にタッチリスナーを設定
            ll_cell.setClickable(true);                             //！これがないとダブルタップが検知されない
            ll_cell.setOnTouchListener(new DateTouchListener( this ));
        }


    }

    /*
     * コンストラクタ
     */
    public CalendarAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        mDaysInMonth = mDateManager.getDays();

        //マーク色
        //mMarkColor = markColor;

        //日付マークリスト

        //選択月の日付マークリスト

    }

    @Override
    public int getCount() {
        //その月の日数
        return mDaysInMonth.size();
    }

    /*
     * セル一つ一つを描画する際にコールされる。
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //findViewById()で取得した参照を保持するためのクラス
        ViewHolder holder;

        //初めて表示されるなら、セルを割り当て。セルはレイアウトファイルを使用。
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.calendar_day_cell, null);
            //convertView = new DateCellView(mContext);

            //ビューを生成。レイアウト内のビューを保持。
            holder = new ViewHolder();
            holder.setView( convertView );
/*
            holder.ll_cell = convertView.findViewById(R.id.ll_cell);
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            holder.v_mark  = convertView.findViewById(R.id.v_mark);
*/

            //タグ設定
            convertView.setTag(holder);

        } else {
            //一度表示されているなら、そのまま活用
            holder = (ViewHolder)convertView.getTag();
        }

        //１週間の日数
        final int WEEK_DAYS = 7;

        //当月の週数
        int weekNum = mDateManager.getWeeks();

        final int MAX_WEKK_NUM = 5;
        if( weekNum >= MAX_WEKK_NUM ){
            //週数が5を超えている場合は、5に丸める
            //(高さを統一するため。なお、2月が4週になっている場合は考慮外）
            //★2022.01 の場合、6行必要のため、要見直し
            weekNum = MAX_WEKK_NUM;
        }

        //セルのサイズを指定
        //画面解像度の比率を取得
        float dp = mContext.getResources().getDisplayMetrics().density;
        //セルの幅と高さ
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                parent.getWidth() / WEEK_DAYS - (int)dp,
                (parent.getHeight() - (int)dp * weekNum ) / weekNum);
                //parent.getHeight());
        convertView.setLayoutParams(params);

        Log.i("CalendarAdapter", "weekNum=" + weekNum + " param.h=" + params.height);

        //日付フォーマット
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);

        //日付セルを初期化
        //holder.clearData( mMarkColor, position );
        holder.clearData( mSelectedMark.getColor(), position, mSelectedMark.getPid() );

        //セルの日付が当月のものである場合
        if (mDateManager.isCurrentMonth(mDaysInMonth.get(position))){

            Log.i("CalendarAdapter", "position=" + position + " 日=" + dateFormat.format(mDaysInMonth.get(position)));

            //日付設定
            holder.tv_date.setText(dateFormat.format(mDaysInMonth.get(position)));

            //デザイン確認用----
/*            if( dateFormat.format(mDaysInMonth.get(position)).equals("1") ){
                holder.v_mark.setVisibility(View.VISIBLE);
            }
            if( dateFormat.format(mDaysInMonth.get(position)).equals("10") ){
                holder.v_mark.setVisibility(View.VISIBLE);
            }
            if( dateFormat.format(mDaysInMonth.get(position)).equals("13") ){
                holder.v_mark.setVisibility(View.VISIBLE);
            }*/
            //デザイン確認用-----

        } else{
            Log.i("CalendarAdapter", "not month position=" + position + " 日=" + dateFormat.format(mDaysInMonth.get(position)));

        }

        //設定したビューを返す(このビューが日付セルとして表示される)
        return convertView;
    }

    //指定位置の日付を取得する
    public String getDate(int position){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        return format.format(mDaysInMonth.get(position));
    }

    //当月を取得
    public String getMonth(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        return format.format(mDateManager.mCalendar.getTime());
    }

    //指定された日の位置を取得
    public int getPositionDate(String target){
        //日付フォーマット
        String format = "yyyy.MM.dd";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.JAPAN);

        //現在表示している日を、文字列型としてリスト化
        List<String> dateArrayString = new ArrayList();

        //リスト生成
        for(int i = 0; i < mDaysInMonth.size(); i++){
            dateArrayString.add(sdf.format(mDaysInMonth.get(i)));
        }

        //指定された日付けの位置を返す。
        return  dateArrayString.indexOf(target);
    }

    //当月の日を取得
    public List<Date> getMonthDays(){
        return mDaysInMonth;
    }

    //翌月表示
    public void nextMonth(){
        //翌月
        mDateManager.nextMonth();
        //保持する日数リストも更新する。
        mDaysInMonth = mDateManager.getDays();

        //自身へ変更通知
        this.notifyDataSetChanged();
    }

    //前月表示
    public void prevMonth(){
        mDateManager.prevMonth();
        mDaysInMonth = mDateManager.getDays();

        //自身へ変更通知
        this.notifyDataSetChanged();
    }

    /*
     * マーク色の設定
     */
    public void setMarkColor( int color ){
        //設定変更
        //mMarkColor = color;
        //自身へ変更通知
        this.notifyDataSetChanged();
    }
    /*
     * マークの設定
     */
    public void setMark( MarkTable mark ){
        //設定変更
        mSelectedMark = mark;
        //自身へ変更通知
        this.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    /*
     * 日付タッチリスナー
     */
    public class DateTouchListener implements View.OnTouchListener {

        //日付セルビュー
        private final ViewHolder viewHolder;
        //ダブルタップ検知用
        private final GestureDetector mDoubleTapDetector;

        /*
         * コンストラクタ
         */
        public DateTouchListener( ViewHolder view ) {

            //日付セルビュー
            viewHolder = view;
            //ダブルタップリスナーを実装したGestureDetector
            mDoubleTapDetector = new GestureDetector(view.tv_date.getContext(), new DoubleTapListener());
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
                if( viewHolder.tv_date.getText().toString().isEmpty() ){
                    return true;
                }

/*                //マークビュー
                MarkView markView = findViewById( R.id.v_mark );

                //マークの付与・削除
                if( markView.getVisibility() == View.INVISIBLE ){
                    markView.setVisibility( View.VISIBLE );
                } else {
                    markView.setVisibility( View.INVISIBLE );
                }*/
                //マークビュー

                //マークの付与・削除
                int preState = viewHolder.v_mark.getVisibility();

                if( preState == View.INVISIBLE ){
                    viewHolder.v_mark.setVisibility( View.VISIBLE );
                } else {
                    viewHolder.v_mark.setVisibility( View.INVISIBLE );
                }

                //保存対象データを生成
                KeepMarkDate markedDate = new KeepMarkDate( mSelectedMark.getPid(), getDate( viewHolder.position ), preState, viewHolder.v_mark.getVisibility() );

                //保存用キューに記録
                CommonData commonData = (CommonData)((Activity) viewHolder.v_mark.getContext()).getApplication();
                commonData.enqueMarkedDate( markedDate );

                //findViewById(R.id.tv_date).

                Log.i("tap", "日付→" + getDate( viewHolder.position ) + " マーク→" + mSelectedMark.getName());



                return true;
            }
        }
    }


}
