package com.mark.markcalendar;

import android.util.Log;

import java.util.ArrayList;

/*
 * 保存対象データ用リスト
 */
public class KeepMarkDateArrayList <E> extends ArrayList<KeepMarkDate> {

    private final int NO_DATA = -1;

    /*
     * コンストラクタ
     */
    public KeepMarkDateArrayList() {
        super();
    }


    /*
     *　マーク処理された日を保存用キューに格納
     *   ※エンキューは以下の観点で行う。
     *　　　・エンキュー対象データの日付がキュー内にない場合、初期状態を設定した上でエンキューする
     */
    public void enqueMarkedDate( KeepMarkDate markedDate ) {

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
        for( KeepMarkDate keep: this ){
            Log.i("enqueMarkedDate", "日付→" + keep.getDate() + " マーク→" + keep.getMarkPid());
        }
        Log.i("enqueMarkedDate", "--------------");
        //----
    }

    /*
     *　指定データの日付が既にリスト内に存在しているかを反映
     *   あり：該当Index
     *   なし：NO_DATA
     */
    public int getMarkedDate( int markPid, String date ) {

        int i = 0;
        for( KeepMarkDate data: this ){
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




}
