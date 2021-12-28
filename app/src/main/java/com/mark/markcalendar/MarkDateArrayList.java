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

    /*
     *　日付マーク情報をもっているかの判定
     *   @para：date（yyyy.mm.dd）
     *
     *   true :あり
     *   false:なし
     */
    public boolean hasMarkDate( String date ) {

        for( MarkDateTable markDate: this ){
            if( date.equals( markDate.getDate() ) ){
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
    public MarkDateArrayList<MarkDateTable> extractDesignatedMark( int markPid) {

        MarkDateArrayList<MarkDateTable> dates = new MarkDateArrayList<>();

        for( MarkDateTable date: this ){

            //指定マークの日付マークをリストに格納
            if( date.getPidPutMark() == markPid ){
                dates.add( date );
            }
        }

        return dates;
    }








}
