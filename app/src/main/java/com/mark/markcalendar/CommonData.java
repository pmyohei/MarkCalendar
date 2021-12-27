package com.mark.markcalendar;

import android.app.Application;

import androidx.annotation.Keep;

/*
 * 共通データ
 */
public class CommonData extends Application {

    //マークリスト
    private MarkArrayList<MarkTable> mMarks;
    //マーク日付保存対象データリスト
    private KeepMarkDateArrayList<KeepMarkDate> mKeepMarkDates;

    /*
     * アプリケーションの起動時に呼び出される
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mMarks = new MarkArrayList<>();
        mKeepMarkDates = new KeepMarkDateArrayList<>();
    }

    /**
     * アプリケーション終了時に呼び出される
     */
    @Override
    public void onTerminate() {
        super.onTerminate();

        mMarks = null;
        mKeepMarkDates = null;
    }

    /*
     *　マークリストを取得
     *
     */
    public MarkArrayList<MarkTable> getMarks() {
        //生成したマークリストを返す
        return mMarks;
    }

    /*
     *　マークリストを指定された順通りに生成する
     *
     */
    public MarkArrayList<MarkTable> createMarksInOrder(MarkArrayList<MarkTable> marks, String order) {

        //指定順通りに生成
        mMarks.addInOrder( marks, order );

        //生成したマークリストを返す
        return mMarks;
    }


    /*
     *　マーク日付保存対象データリストを取得
     */
    public KeepMarkDateArrayList<KeepMarkDate> getKeepMarkDates() {
        return mKeepMarkDates;
    }

    /*
     *　マーク処理された日を保存用キューに格納
     */
    public void enqueMarkedDate( KeepMarkDate markedDate ) {
        mKeepMarkDates.enqueMarkedDate( markedDate );
    }

}
