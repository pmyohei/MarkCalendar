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
     * 月ごとのマーク数情報リストを生成する
     */
    public MonthMarkArrayList<MonthMarkInformation> createMonthMarkData(){

        //月ごとマーク数情報リスト
        MonthMarkArrayList<MonthMarkInformation> monthMarks = new MonthMarkArrayList<>();

        //日付マーク数分繰り返し
        for( MarkDateTable markDate: this ){

            //年月のみ取得
            String yearMonth = markDate.getDate().substring(0, ResourceData.YEAR_MONTH_CHAR_NUM);

            //日付マークの日付の年月を既にリストに格納しているかチェック
            int idx = monthMarks.hasMonth( yearMonth );
            if( idx == MonthMarkArrayList.NO_DATA ){
                //年月マーク情報を生成
                MonthMarkInformation month = new MonthMarkInformation( yearMonth, 1 );
                //リストに追加
                monthMarks.add( month );

            } else {
                //既にリストに格納済みであれば、マーク数を加算
                monthMarks.get(idx).incrementMarkNum();
            }
        }

        //----
        for( MonthMarkInformation tmp: monthMarks ){
            Log.i("MonthMark", "ソート前 年月→" + tmp.getYearMonth() + " マーク→" + tmp.getMarkNum());
        }
        Log.i("MonthMark", "-------------");
        //----

        //生成されたリストを年月で降順にする
        Collections.sort(monthMarks, new MonthComparator());

        //----
        for( MonthMarkInformation tmp: monthMarks ){
            Log.i("MonthMark", "ソート後 年月→" + tmp.getYearMonth() + " マーク→" + tmp.getMarkNum());
        }
        Log.i("MonthMark", "-------------");
        //----

        //現時点の年月
        Date now = new Date();
        Calendar afterCal = Calendar.getInstance();
        afterCal.setTime( now );

        //空いた年月にマーク数なしを追加
        MonthMarkArrayList<MonthMarkInformation> fullMonthMarks = new MonthMarkArrayList<>();
        for( MonthMarkInformation monthMark: monthMarks ){

            Date monthDate;
            try {
                //リストの年月をDateに変換
                monthDate = ResourceData.sdf_yearMonth.parse( monthMark.getYearMonth() );
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

            //カレンダーに変換
            Calendar beforeCal = Calendar.getInstance();
            beforeCal.setTime( monthDate );

            //月差を取得
            int diffMonth = ResourceData.diffMonth( afterCal, beforeCal );

            Log.i("MonthMark", "diffMonth()=" + diffMonth);

            for( int i = 0; i < diffMonth; i++ ){

                //年月マーク情報を生成
                Date date = afterCal.getTime();
                String checkDateStr = ResourceData.sdf_yearMonth.format( date );
                MonthMarkInformation month = new MonthMarkInformation( checkDateStr, 0 );

                //リストに追加
                fullMonthMarks.add( month );

                //前の月へ
                afterCal.add( Calendar.MONTH, -1 );
            }

            //前の月へ
            afterCal.add( Calendar.MONTH, -1 );

            //マーク数ありのデータを追加
            fullMonthMarks.add( monthMark );
        }

        //----
        for( MonthMarkInformation tmp: fullMonthMarks ){
            Log.i("MonthMark", "完成形 年月→" + tmp.getYearMonth() + " マーク→" + tmp.getMarkNum());
        }
        //----

        return fullMonthMarks;
    }
}
