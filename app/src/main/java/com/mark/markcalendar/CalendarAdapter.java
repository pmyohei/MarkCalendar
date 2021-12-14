package com.mark.markcalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
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

    /*
     * 日付レイアウト用クラス
     */
    private static class ViewHolder {
        public TextView tv_date;
        public MarkView v_mark;
    }

    /*
     * コンストラクタ
     */
    public CalendarAdapter(Context context){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        mDaysInMonth = mDateManager.getDays();
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

            //ビューを生成。レイアウト内のビューを保持。
            holder = new ViewHolder();
            holder.tv_date = convertView.findViewById(R.id.tv_date);
            holder.v_mark  = convertView.findViewById(R.id.v_mark);

            //タグ設定
            convertView.setTag(holder);

        } else {
            //一度表示されているなら、そのまま活用
            holder = (ViewHolder)convertView.getTag();
        }

        //１週間の日数
        final int WEEK_DAYS_NUM = 7;

        //当月の週数
        int weekNum = mDateManager.getWeeks();

        //セルのサイズを指定
        //画面解像度の比率を取得
        float dp = mContext.getResources().getDisplayMetrics().density;
        //セルの幅と高さ
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                parent.getWidth() / WEEK_DAYS_NUM - (int)dp,
                (parent.getHeight() - (int)dp * weekNum ) / weekNum);
                //parent.getHeight());
        convertView.setLayoutParams(params);

        //日付フォーマット
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);

        //日付セルを初期化
        holder.tv_date.setText("");
        holder.v_mark.setVisibility(View.INVISIBLE);

        //セルの日付が当月のものである場合
        if (mDateManager.isCurrentMonth(mDaysInMonth.get(position))){

            Log.i("CalendarAdapter", "position=" + position + " 日=" + dateFormat.format(mDaysInMonth.get(position)));

            //日付設定
            holder.tv_date.setText(dateFormat.format(mDaysInMonth.get(position)));

            //デザイン確認用----
            if( dateFormat.format(mDaysInMonth.get(position)).equals("1") ){
                holder.v_mark.setVisibility(View.VISIBLE);
            }
            if( dateFormat.format(mDaysInMonth.get(position)).equals("10") ){
                holder.v_mark.setVisibility(View.VISIBLE);
            }
            if( dateFormat.format(mDaysInMonth.get(position)).equals("13") ){
                holder.v_mark.setVisibility(View.VISIBLE);
            }
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

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


}
