package com.mark.markcalendar;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncReadMark {

    //マーク指定なし
    private final int NO_MARK = -1;

    private final AppDatabase                   mDB;
    private final onFinishListener              mOnFinishListener;
    private final int                           mMarkPid;
    private MarkArrayList<MarkTable>            mMarks;
    private MarkDateArrayList<MarkDateTable>    mMarkDates;

    /*
     * コンストラクタ
     */
    public AsyncReadMark(Context context, onFinishListener listener) {
        mDB               = AppDatabaseManager.getInstance(context);
        mOnFinishListener = listener;
        mMarkPid          = NO_MARK;
        mMarks = new MarkArrayList<>();
        mMarkDates = new MarkDateArrayList<>();
    }

    /*
     * コンストラクタ
     */
    public AsyncReadMark(Context context, int markPid, onFinishListener listener) {
        mDB               = AppDatabaseManager.getInstance(context);
        mMarkPid          = markPid;
        mOnFinishListener = listener;

        mMarkDates = new MarkDateArrayList<>();
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

            //MarkDateTableDao
            MarkDateTableDao markDateTableDao = mDB.daoMarkDateTable();

            //マーク指定なしの場合
            if( mMarkPid == NO_MARK ){
                //MarkDao
                MarkTableDao markDao = mDB.daoMarkTable();

                //登録中マークを全て取得
                List<MarkTable> marks = markDao.getAll();
                mMarks.addAll( marks );

                //全日付マークを取得
                List<MarkDateTable> markDates = markDateTableDao.getAll();
                mMarkDates.addAll( markDates );

            } else {
                //指定マークの日付マークを取得
                List<MarkDateTable> markDates = markDateTableDao.getMarkDateOfMark( mMarkPid );
                mMarkDates.addAll( markDates );
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
        mOnFinishListener.onFinish( mMarks, mMarkDates );
    }

    /*
     * データ作成完了リスナー
     */
    public interface onFinishListener {
        /*
         * 生成完了時、コールされる
         */
        void onFinish( MarkArrayList<MarkTable> marks, MarkDateArrayList<MarkDateTable> markDatess );
    }


}
