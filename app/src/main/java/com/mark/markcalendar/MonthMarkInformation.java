package com.mark.markcalendar;

import java.util.Comparator;

public class MonthMarkInformation {

    private String yearMonth;
    private int    markNum;

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
    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public int getMarkNum() {
        return markNum;
    }
    public void setMarkNum(int markNum) {
        this.markNum = markNum;
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