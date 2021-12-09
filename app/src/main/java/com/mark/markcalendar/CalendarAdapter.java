package com.mark.markcalendar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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

    private List<Date> dateArray = new ArrayList();
    private Context mContext;
    private DateManager mDateManager;
    private LayoutInflater mLayoutInflater;

    //カスタムセルを拡張したらここでWigetを定義
    private static class ViewHolder {
        public TextView dateText;
        //public long     title_id;
    }

    public CalendarAdapter(Context context){
        mContext = context;
        //描画高速化のために必要
        mLayoutInflater = LayoutInflater.from(mContext);
        //カレンダー管理用
        mDateManager = new DateManager();
        //その月の日にちをリストで保持
        dateArray = mDateManager.getDays();
    }

    @Override
    public int getCount() {
        //その月の日の数
        return dateArray.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //これは、セル一つ一つを描画する際にコールされる。

        //findViewById()で取得した参照を保持するためのクラス
        ViewHolder holder;
        //初めて表示されるなら、セルを割り当て。セルはレイアウトファイルを使用する。
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.calendar_cell, null);

            //割り当てたセルにタグをつける
            holder = new ViewHolder();
            holder.dateText = convertView.findViewById(R.id.dateText);
            convertView.setTag(holder);

        } else {
            //一度、表示されているなら、それをそのまま活用
            holder = (ViewHolder)convertView.getTag();
        }

        //セルのサイズを指定
        //画面解像度の比率を取得
        float dp = mContext.getResources().getDisplayMetrics().density;
        //セルの幅と高さ
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                parent.getWidth()/7 - (int)dp,
                (parent.getHeight() - (int)dp * mDateManager.getWeeks() ) / mDateManager.getWeeks());
        convertView.setLayoutParams(params);

        //日付のみ表示させる
        SimpleDateFormat dateFormat = new SimpleDateFormat("d", Locale.US);
        holder.dateText.setText(dateFormat.format(dateArray.get(position)));

        //セルに対する色の設定
        if (mDateManager.isCurrentMonth(dateArray.get(position))){
            //今月のセルをまず色付け
            convertView.setBackgroundColor(Color.WHITE);

        }else {
            //先月・来月のセルは、グレーアウト
            convertView.setBackgroundColor(parent.getResources().getColor(R.color.gray));
        }

        //日曜日を赤、土曜日を青に
        int colorId;
        Typeface boldtype;
        switch (mDateManager.getDayOfWeek(dateArray.get(position))){
            case 1:
                colorId = Color.RED;
                break;

            case 7:
                colorId = Color.BLUE;
                break;

            default:
                if(mDateManager.isCurrentDay(dateArray.get(position))) {
                    //今日の日付のみ、黒とする
                    colorId = Color.BLACK;
                }else{
                    colorId = Color.GRAY;
                }
                break;
        }

        //今日の日付のみ、太字
        if(mDateManager.isCurrentDay(dateArray.get(position))) {
            boldtype = Typeface.DEFAULT_BOLD;
        }else{
            boldtype = Typeface.DEFAULT;
        }

        //色と太さを設定設定
        holder.dateText.setTextColor(colorId);
        holder.dateText.setTypeface(boldtype);

        return convertView;
    }

    //指定位置の日付を取得する
    public String getDate(int position){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        return format.format(dateArray.get(position));

        //Date型で返す
        //return dateArray.get(position);
    }

    //表示月を取得
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
        for(int i = 0; i < dateArray.size(); i++){
            dateArrayString.add(sdf.format(dateArray.get(i)));
        }

        //指定された日付けの位置を返す。
        return  dateArrayString.indexOf(target);
    }

    //表示月の日を取得
    public List<Date> getMonthDays(){
        return dateArray;
    }

    //翌月表示
    public void nextMonth(){
        //翌月
        mDateManager.nextMonth();
        //保持する日数リストも更新する。
        dateArray = mDateManager.getDays();
        this.notifyDataSetChanged();
    }

    //前月表示
    public void prevMonth(){
        mDateManager.prevMonth();
        dateArray = mDateManager.getDays();
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
