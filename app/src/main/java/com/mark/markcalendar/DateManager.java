package com.mark.markcalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/*
 * カレンダー管理クラス
 */
public class DateManager {

    Calendar         mCalendar;          //カレンダー
    String           mCurrentDay;        //今日の日付
    SimpleDateFormat msdf_date;          //年月日文字列フォーマット

    /*
     * コンストラクタ
     */
    public DateManager(){
        //インスタンス生成時は、現在の日付と時間で初期化する
        mCalendar = Calendar.getInstance();

        //今日の日付を保持
        msdf_date = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        mCurrentDay = msdf_date.format(mCalendar.getTime());
    }

    /*
     * 当月の要素を取得
     */
    public List<Date> getDays(){
        //現在日時を保持
        Date startDate = mCalendar.getTime();

        //当月のカレンダーに表示される前月分の日数を計算
        mCalendar.set(Calendar.DATE, 1);                            //表示する月の月初めの年月日に設定
        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;    //月初めの曜日の一つ前の曜日 を取得 = この値は、1行目の前の月の表示日数になる。
        mCalendar.add(Calendar.DATE, -dayOfWeek);                   //セルの描画の開始日時を、表示数分だけ遡る。

        //当月カレンダーとして表示する日付リスト（先月後半／次月前半も含む）
        List<Date> days = new ArrayList<>();

        //GridViewに表示するマスの合計を計算
        int count = getWeeks() * 7 ;

        //セルの描画数分だけリストに格納する。
        for (int i = 0; i < count; i ++){
            //日情報として追加
            days.add(mCalendar.getTime());
            //カレンダーを１日進める
            mCalendar.add(Calendar.DATE, 1);
        }

        //描画のための計算を終えたので、計算前の状態に戻す
        mCalendar.setTime(startDate);

        //画面に描画する日にちを返す
        return days;
    }

    /*
     * 今日かどうか判定
     */
    public boolean isCurrentDay(Date date){
        //保持している今日の日付(アプリを起動したときの日付)と比較
        return mCurrentDay.equals(msdf_date.format(date));
    }

    /*
     * 当月かどうか判定
     */
    public boolean isCurrentMonth(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);

        //画面に表示中のカレンダーが保持している時間の年月を取得
        String currentMonth = format.format(mCalendar.getTime());

        return currentMonth.equals(format.format(date));
    }

    /*
     * 月の週数を取得
     */
    public int getWeeks(){
        return mCalendar.getActualMaximum(Calendar.WEEK_OF_MONTH);
    }

    //曜日を取得
    public int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //翌月へ(カレンダーの月に1月加算して、翌月の情報を保持する)
    public void nextMonth(){
        mCalendar.add(Calendar.MONTH, 1);
    }

    //前月へ(カレンダーの月に1月減算して、前月の情報を保持する)
    public void prevMonth(){
        mCalendar.add(Calendar.MONTH, -1);
    }

}
