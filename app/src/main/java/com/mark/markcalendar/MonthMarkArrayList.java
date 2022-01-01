package com.mark.markcalendar;

import java.util.ArrayList;

public class MonthMarkArrayList<E> extends ArrayList<MonthMarkInformation> {

    /* 定数 */
    public static final int NO_DATA = -1;               //データなし

    /*
     * コンストラクタ
     */
    public MonthMarkArrayList() {
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
     *　指定年月のデータを持っているか
     */
    public int hasMonth( String yearMonth ) {

        int i = 0;
        for( MonthMarkInformation month: this  ){

            //年月が一致すれば、そのindexを返す
            //String listYearMonth = month.getYearMonth().substring(0, ResourceData.YEAR_MONTH_CHAR_NUM - 1);
            String listYearMonth = month.getYearMonth();
            if( listYearMonth.equals( yearMonth ) ){
                return i;
            }

            i++;
        }

        return NO_DATA;
    }

}
