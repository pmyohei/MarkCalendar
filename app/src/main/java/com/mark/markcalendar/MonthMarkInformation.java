package com.mark.markcalendar;

import java.util.Comparator;

public class MonthMarkInformation {

    private final String yearMonth;   //年月（yyyy.mm）
    private int          markNum;     //文字列

    public MonthMarkInformation( String yearMonth, int markNum ){
        this.yearMonth = yearMonth;
        this.markNum   = markNum;
    }

    /*
     *　マーク数をインクリメント
     */
    public void incrementMarkNum() {
        this.markNum++;
    }

    /*-- getter/setter --*/
    public String getYearMonth() {
        return yearMonth;
    }
    public int getMarkNum() {
        return markNum;
    }
}

class MonthComparator implements Comparator<MonthMarkInformation> {

    /*
     * 並び替え
     */
    public int compare(MonthMarkInformation c1, MonthMarkInformation c2) {

        if( c1.getYearMonth().compareTo( c2.getYearMonth() ) < 0 ){
            return 1;
        } else {
            return -1;
        }
    }
}