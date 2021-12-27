package com.mark.markcalendar;

import java.util.ArrayList;

public class MarkDateArrayList<E> extends ArrayList<MarkDateTable> {

    /* 定数 */
    public static final int     NO_DATA = -1;               //データなし

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





}
