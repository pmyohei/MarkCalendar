package com.mark.markcalendar;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncReadMark {

    private final AppDatabase                   mDB;
    private final onFinishListener              mOnFinishListener;
    private final int                           mSelectedMarkPid;
    private MarkArrayList<MarkTable>            mMarks;
    private MarkDateArrayList<MarkDateTable>    mMarkDates;

    /*
     * コンストラクタ
     */
    public AsyncReadMark(Context context, int markPid, onFinishListener listener) {
        mDB               = AppDatabaseManager.getInstance(context);
        mSelectedMarkPid  = markPid;
        mOnFinishListener = listener;

        mMarks = new MarkArrayList<>();
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
            //MarkDao
            MarkTableDao markDao = mDB.daoMarkTable();

            //登録中マークを全て取得
            List<MarkTable> marks = markDao.getAll();
            mMarks.addAll( marks );

            //前回選択中マーク
            int selectedPid = mSelectedMarkPid;
            if( selectedPid == CalendarActivity.INVALID_SELECTED_MARK ){
                //取得に失敗した場合は、先頭のマークにする
                selectedPid = mMarks.get(0).getPid();
            }

            //前回選択中マークの日付マーク情報を全て取得
            List<MarkDateTable> markDates = markDateTableDao.getMarkDateOfMark( selectedPid );
            mMarkDates.addAll( markDates );
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
