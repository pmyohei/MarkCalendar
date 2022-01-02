package com.mark.markcalendar;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncUpdateMarkDate {

    private final AppDatabase               mDB;
    private final TapDataArrayList<TapData> mTapData;
    private final onFinishListener          mOnFinishListener;

    /*
     * コンストラクタ
     */
    public AsyncUpdateMarkDate(Context context, TapDataArrayList<TapData> tapData, onFinishListener listener) {
        mDB               = AppDatabaseManager.getInstance(context);
        mOnFinishListener = listener;
        mTapData          = tapData;
    }

    /*
     * 非同期処理
     */
    private class AsyncRunnable implements Runnable {

        Handler handler = new Handler(Looper.getMainLooper());

        /*
         * バックグラウンド処理
         */
        @Override
        public void run() {

            //メイン処理
            dbOperation();

            //後処理
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onPostExecute();
                }
            });
        }

        /*
         * DB処理
         */
        private void dbOperation(){

            //Dao
            MarkDateTableDao markDateTableDao =  mDB.daoMarkDateTable();

            //マーク状態の更新
            for( TapData tapData: mTapData){

                if( tapData.isMarked() ){
                    //マークが付けられたなら、追加

                    //日付マーク生成
                    MarkDateTable date = new MarkDateTable();
                    date.setDate( tapData.getDate() );
                    date.setPidPutMark( tapData.getMarkPid() );

                    //挿入
                    markDateTableDao.insert( date );

                } else {
                    //マークが消されたなら、削除
                    markDateTableDao.deleteByDate( tapData.getMarkPid(), tapData.getDate() );
                }
            }
        }
    }

    /*
     * バックグラウンド前処理
     */
    void onPreExecute() {
        //
    }

    /*
     * 実行
     */
    void execute() {
        //バックグランド前処理
        onPreExecute();

        //シングルスレッド（キューなし）で動作するexecutorを作成
        ExecutorService executorService  = Executors.newSingleThreadExecutor();

        //非同期処理を送信
        executorService.submit(new AsyncRunnable());
    }

    /*
     * バックグランド処理終了後の処理
     */
    void onPostExecute() {

        //読み取り完了
        mOnFinishListener.onFinish();
    }

    /*
     * データ作成完了リスナー
     */
    public interface onFinishListener {
        /*
         * 生成完了時、コールされる
         */
        void onFinish();
    }


}
