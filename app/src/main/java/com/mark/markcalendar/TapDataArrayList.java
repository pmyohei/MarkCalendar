package com.mark.markcalendar;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/*
 * 保存対象データ用リスト
 */
public class TapDataArrayList<E> extends ArrayList<TapData> {

    public static final int NO_DATA = -1;

    /*
     * コンストラクタ
     */
    public TapDataArrayList() {
        super();
    }


    /*
     *　マーク処理された日を保存用キューに格納
     *   ※エンキューは以下の観点で行う。
     *　　　・エンキュー対象データの日付がキュー内にない場合、初期状態を設定した上でエンキューする
     */
    public void enqueMarkedDate( TapData markedDate ) {

        //指定マークが既にあるか確認
        int existingIdx = getMarkedDate(markedDate.getMarkPid(), markedDate.getDate()  );

        if( existingIdx == NO_DATA ){
            //新規データとして追加
            add(markedDate);

        } else {
            //既にある場合は、現在の状態のみ更新
            //get(existingIdx).setCurrentState( markedDate.getCurrentState() );

            //既にある場合は、状態に変化がなくなっている状態のため、リストから削除
            remove( existingIdx );
        }

        //----
        //for( TapData keep: this ){
        //    Log.i("enqueMarkedDate", "日付→" + keep.getDate() + " マーク→" + keep.getMarkPid());
        //}
        //Log.i("enqueMarkedDate", "--------------");
        //----
    }

    /*
     *　指定マークの日付が既にリスト内に存在しているか
     *   あり：該当Index
     *   なし：NO_DATA
     */
    public int getMarkedDate( int markPid, String date ) {

        int i = 0;
        for( TapData data: this ){
            //マークPidと日付が一致する場合
            if( (data.getDate().equals( date ) ) && ( data.getMarkPid() == markPid ) ){
                //Indexを返す
                return i;
            }

            i++;
        }

        //リスト内になし
        return NO_DATA;
    }

    /*
     *　指定データの日付が既にリスト内に存在しているか
     *   あり：該当Index
     *   なし：null
     */
    public Integer checkDateState( int markPid, String date ) {

        int idx = getMarkedDate(markPid, date);
        if( idx == NO_DATA ){
            return null;
        }

        return get(idx).getCurrentState();
    }

    /*
     *　リスト中の指定マークにおける指定月のマーク数を取得（マイナスあり）
     */
    public int getMarkedDateNum( int markPid, String yearMonth ) {

        int count = 0;

        for( TapData tap: this ){

            //タップ情報の年月を取得
            String tapYearMonth = tap.getDate().substring(0, ResourceData.YEAR_MONTH_CHAR_NUM);

            if( (tap.getMarkPid() != markPid) || !tapYearMonth.equals(yearMonth) ){
                //指定マークの指定月以外なら、次のデータへ
                continue;
            }

            //表示中なら加算、非表示なら減算
            if( tap.getCurrentState() == View.VISIBLE ){
                count++;
            } else{
                count--;
            }
        }

        return count;
    }


}
