package com.mark.markcalendar;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ResourceData {

    public static final String YEAR_MONTH_DAY = "yyyy.MM.dd";
    public static final String YEAR_MONTH     = "yyyy.MM";

    public static final int YEAR_MONTH_CHAR_NUM = 7;    //年月文字列の文字数「yyyy.MM」

    public static SimpleDateFormat sdf_yearMonthDay = new SimpleDateFormat(YEAR_MONTH_DAY, Locale.US);
    public static SimpleDateFormat sdf_yearMonth    = new SimpleDateFormat(YEAR_MONTH, Locale.US);


    /*
     * 月差を取得
     */
    public static int diffMonth(Calendar afterCal, Calendar beforeCal){

        Log.i("MonthMark", "afterCal=" + ResourceData.sdf_yearMonth.format( afterCal.getTime() ));
        Log.i("MonthMark", "beforeCal=" + ResourceData.sdf_yearMonth.format( beforeCal.getTime() ));

        //同じ年月の場合でも、必ず1周するため、初期値は以下にする
        int count = -1;

        //前の可能性のあるカレンダーが、後のカレンダーより前である内はループ
        while (beforeCal.before(afterCal)) {
            //前カレンダーを1月進める
            beforeCal.add(Calendar.MONTH, 1);
            count++;
        }

        return count;
    }

}
