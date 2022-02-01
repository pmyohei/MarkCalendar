package com.mark.markcalendar;

import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class MarkDateArrayList<E> extends ArrayList<MarkDateTable> {

    /* 定数 */
    public static final int NO_DATA = -1;               //データなし

    /*
     * コンストラクタ
     */
    public MarkDateArrayList() {
        super();
    }

    /*
     *　ラストIndex取得
     */
    public int getLastIdx() {

        int size = size();

        if (size == 0) {
            return NO_DATA;
        }

        return size - 1;
    }

    /*
     *　日付マーク情報をもっているかの判定
     *   @para：date（yyyy.mm.dd）
     *
     *   true :あり
     *   false:なし
     */
    public boolean hasMarkDate(String date) {

        for (MarkDateTable markDate : this) {
            if (date.equals(markDate.getDate())) {
                //日付の一致するデータあり
                return true;
            }
        }

        //データなし
        return false;
    }

    /*
     *　指定マークの日付マークリストを抽出
     */
    public MarkDateArrayList<MarkDateTable> extractDesignatedMark(int markPid) {

        MarkDateArrayList<MarkDateTable> dates = new MarkDateArrayList<>();

        for (MarkDateTable date : this) {

            //指定マークの日付マークをリストに格納
            if (date.getPidPutMark() == markPid) {
                dates.add(date);
            }
        }

        return dates;
    }

    /*
     *　指定月のマーク数を取得
     */
    public MarkDateArrayList<MarkDateTable> extractDesignatedMonth(String yearMonth) {

        MarkDateArrayList<MarkDateTable> dates = new MarkDateArrayList<>();

        for (MarkDateTable date : this) {
            //年月のみ取得
            String tmpYearMonth = date.getDate().substring(0, ResourceData.YEAR_MONTH_CHAR_NUM);

            if (tmpYearMonth.equals(yearMonth)) {
                //年月が一致すれば、リストに追加
                dates.add(date);
            }
        }

        return dates;
    }

    /*
     *　指定月のマーク数を取得
     */
    public int getMonthNum(String yearMonth) {

        int count = 0;

        for (MarkDateTable date : this) {
            //年月のみ取得
            String tmpYearMonth = date.getDate().substring(0, ResourceData.YEAR_MONTH_CHAR_NUM);

            if (tmpYearMonth.equals(yearMonth)) {
                //年月が一致すれば、カウント
                count++;
            }
        }

        return count;
    }


    /*
     * 月ごとのマーク数統計リストを生成する
     */
    public MonthMarkArrayList<MonthMarkInformation> createMonthMarkData() {

        //月ごとマーク数情報リスト
        MonthMarkArrayList<MonthMarkInformation> monthMarks = new MonthMarkArrayList<>();

        //日付マーク数分繰り返し
        for (MarkDateTable markDate : this) {

            //年月文字列のみ取得(先頭から切り取る形で取得)
            String yearMonth = markDate.getDate().substring(0, ResourceData.YEAR_MONTH_CHAR_NUM);

            //日付マークの日付の年月を既にリストに格納しているかチェック
            int idx = monthMarks.hasMonth(yearMonth);
            if (idx == MonthMarkArrayList.NO_DATA) {
                //年月マーク情報を生成
                MonthMarkInformation month = new MonthMarkInformation(yearMonth, 1);
                //リストに追加
                monthMarks.add(month);

            } else {
                //既にリストに格納済みであれば、マーク数を加算
                monthMarks.get(idx).incrementMarkNum();
            }
        }

        //----
        //for (MonthMarkInformation tmp : monthMarks) {
        //    Log.i("MonthMark", "ソート前 年月→" + tmp.getYearMonth() + " マーク→" + tmp.getMarkNum());
        //}
        //Log.i("MonthMark", "-------------");
        //----

        //生成されたリストを年月で降順にする
        Collections.sort(monthMarks, new MonthComparator());

        //マーク数のない月をマーク数0件のデータで埋める
        return paddingNoMarkMonth(monthMarks);
    }

    /*
     * マーク数のない月をマーク数0件のデータで埋める
     */
    public MonthMarkArrayList<MonthMarkInformation> paddingNoMarkMonth( MonthMarkArrayList<MonthMarkInformation> onMarkMonths ) {
        //----
        //for (MonthMarkInformation tmp : onMarkMonths) {
        //    Log.i("MonthMark", "ソート後 年月→" + tmp.getYearMonth() + " マーク→" + tmp.getMarkNum());
        //}
        //Log.i("MonthMark", "-------------");
        //----

        //完成予定リスト
        MonthMarkArrayList<MonthMarkInformation> fullMonthMarks = new MonthMarkArrayList<>();

        //マーク数なしなら空のリストを返す
        if (onMarkMonths.size() == 0) {
            return fullMonthMarks;
        }

        //現時点の年月をチェック対象の年月とする
        Date now = new Date();
        Calendar checkMonth = Calendar.getInstance();
        checkMonth.setTime(now);

        Date monthDate;
        try {
            //リスト先頭（最新の）年月を取得
            monthDate = ResourceData.sdf_yearMonth.parse(onMarkMonths.get(0).getYearMonth());
        } catch (ParseException e) {
            e.printStackTrace();
            return fullMonthMarks;
        }

        //一番最新の年月をカレンダー化
        Calendar latestMonth = Calendar.getInstance();
        latestMonth.setTime(monthDate);

        //最新年月が、現在年月よりも新しければ、チェック年月を持ち替え
        if (latestMonth.after(checkMonth)) {
            checkMonth = latestMonth;
        }

        //空いた年月にマーク数0件のデータを追加していく
        for ( int i = 0; i < onMarkMonths.size(); ) {

            MonthMarkInformation markMonth = onMarkMonths.get(i);

            //チェック年月を文字列に変換
            String checkMonthStr = ResourceData.sdf_yearMonth.format( checkMonth.getTime() );

            //Log.i("MonthMark", "チェック年月 loop→" + checkMonthStr);
            //Log.i("MonthMark", "マーク年月 loop→" + markMonth.getYearMonth());

            //チェック年月の方が、マークあり年月よりも未来
            if( checkMonthStr.compareTo( markMonth.getYearMonth() )  > 0 ){

                //マーク数0件の年月マーク情報を生成
                Date date = checkMonth.getTime();
                String checkDateStr = ResourceData.sdf_yearMonth.format(date);
                MonthMarkInformation month = new MonthMarkInformation(checkDateStr, 0);

                //Log.i("MonthMark", "マーク年月 loop 空");

                //マーク0件の年月の情報を追加
                fullMonthMarks.add(month);

            } else {

                //マークあり年月をリストに追加
                fullMonthMarks.add(markMonth);
                //次のマークあり年月へ
                i++;
            }

            //チェック年月を前の月へ
            checkMonth.add(Calendar.MONTH, -1);
        }

        //----
        //for (MonthMarkInformation tmp : fullMonthMarks) {
        //    Log.i("MonthMark", "完成形 年月→" + tmp.getYearMonth() + " マーク→" + tmp.getMarkNum());
        //}
        //----

        return fullMonthMarks;
    }
}
