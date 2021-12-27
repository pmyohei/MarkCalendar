package com.mark.markcalendar;

import android.view.View;

/*
 * マーク日付保存対象データ
 */
public class KeepMarkDate {

    private int     markPid;        //対象マーク
    private String  date;           //操作がはいった日付
    //private int     initState;      //日付の初期状態（Visible/Invisible）
    private int     currentState;   //現在の状態（Visible/Invisible）

    /*
     * コンストラクタ
     */
    public KeepMarkDate( int markPid, String date, int currentState ){
        this.markPid      = markPid;
        this.date         = date;
        //this.initState    = initState;
        this.currentState = currentState;
    }


    /*
     * マークを付ける
     *   true ：マーク付与
     *   false：マーク削除
     */
    public boolean isMarked() {

        if( getCurrentState() == View.VISIBLE ){
            //マークが付けられた状態
            return true;
        } else {
            //マークが消された状態
            return false;
        }
    }

    /*-- getter／setter --*/
    public int getMarkPid() {
        return markPid;
    }
    public void setMarkPid(int markPid) {
        this.markPid = markPid;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

/*
    public int getInitState() {
        return initState;
    }
    public void setInitState(int initState) {
        this.initState = initState;
    }
*/

    public int getCurrentState() {
        return currentState;
    }
    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }
}
