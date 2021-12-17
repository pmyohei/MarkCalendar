package com.mark.markcalendar;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncReadMark {

    private final AppDatabase         mDB;
    private final onFinishListener    mOnFinishListener;
    private List<MarkTable>           mMarks;

    /*
     * コンストラクタ
     */
    public AsyncReadMark(Context context, onFinishListener listener) {
        mDB               = AppDatabaseManager.getInstance(context);
        mOnFinishListener = listener;

        mMarks = new ArrayList<>();
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

            //MarkDao
            MarkTableDao markDao = mDB.daoMarkTable();

            //ノードを挿入し、レコードに割り当てられたpidを取得
            mMarks = markDao.getAll();
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
        mOnFinishListener.onFinish( mMarks );
    }

    /*
     * データ作成完了リスナー
     */
    public interface onFinishListener {
        /*
         * 生成完了時、コールされる
         */
        void onFinish( List<MarkTable> marks );
    }


}
