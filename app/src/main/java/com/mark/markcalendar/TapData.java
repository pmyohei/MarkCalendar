package com.mark.markcalendar;

import android.view.View;

/*
 * マーク日付保存対象データ
 */
public class TapData {

    private int     markPid;        //対象マーク
    private String  date;           //操作がはいった日付
    private int     currentState;   //現在の状態（Visible/Gone）

    /*
     * コンストラクタ
     */
    public TapData(int markPid, String date, int currentState ){
        this.markPid      = markPid;
        this.date         = date;
        this.currentState = currentState;
    }


    /*
     * マークの有無
     *   true ：マークあり
     *   false：マークなし
     */
    public boolean isMarked() {
        //マークの有無を返す
        return getCurrentState() == View.VISIBLE;
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


    public int getCurrentState() {
        return currentState;
    }
    public void setCurrentState(int currentState) {
        this.currentState = currentState;
    }
}
