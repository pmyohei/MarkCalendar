package com.mark.markcalendar;

import static android.content.Context.VIBRATOR_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {

    private final Context                       mContext;                       //コンテキスト
    private List<Date>                          mDaysInMonth = new ArrayList(); //選択月の日リスト
    private final DateManager                   mDateManager;                   //カレンダー管理用
    private final LayoutInflater                mLayoutInflater;                //描画高速化のために必要
    private MarkTable                           mSelectedMark;                  //選択中マーク
    private MarkDateArrayList<MarkDateTable>    mMarkDates;                     //日付マーク（全情報）
    private MarkDateArrayList<MarkDateTable>    mShowMarkDates;                 //日付マーク（選択中マーク）
    private MarkCountView                       mMarkCountView;                 //マーク数表示ビュー

    /*
     * 日付レイアウトクラス
     */
    private class ViewHolder {

        //セル位置
        private int position;
        //日付セル内のビュー
        public LinearLayout ll_cell;
        public TextView tv_date;
        public MarkView v_mark;

        /*
         * ビューの設定
         */
        @SuppressLint("ClickableViewAccessibility")
        public void setView(View view) {
            ll_cell = view.findViewById(R.id.ll_cell);
            tv_date = view.findViewById(R.id.tv_date);
            v_mark = view.findViewById(R.id.v_mark);

            //レイアウト全体にタッチリスナーを設定
            //ll_cell.setClickable(true);                             //！これがないとダブルタップが検知されない
            //ll_cell.setOnTouchListener(new DateTouchListener( this ));
        }

        /*
         * 表示情報の初期化
         */
        public void clearData(int position) {

            //セル位置
            this.position = position;

            //ビューの表示初期化
            tv_date.setText("");
            tv_date.setBackground( null );
            v_mark.setVisibility(View.GONE);

            //マークが選択されている時だけ、以下を設定
            if (mSelectedMark != null) {
                //マーク色の設定
                v_mark.setColorHex(mSelectedMark.getColor());

                //レイアウト全体にタッチリスナーを設定
                ll_cell.setClickable(true);                             //！これがないとダブルタップが検知されない
                ll_cell.setOnTouchListener(new DateTouchListener(this));
            }
        }

        /*
         * 日付情報の設定
         */
        public void setDateInfo(int position) {

            //日付フォーマット
            SimpleDateFormat sdf_d = new SimpleDateFormat("d", Locale.US);
            //日付
            tv_date.setText(sdf_d.format(mDaysInMonth.get(position)));

            //マークが選択されている場合
            if (mSelectedMark != null) {

                //対象の年月日情報
                String date = getDate(position);

                //マークの有無チェック①：DB内の情報
                if (mShowMarkDates.hasMarkDate(date)) {
                    //対象日のデータがあれば、マークを表示
                    v_mark.setVisibility(View.VISIBLE);
                }

                //マークの有無チェック②：ユーザー操作の情報
                CommonData commonData = (CommonData) ((Activity) v_mark.getContext()).getApplication();
                TapDataArrayList<TapData> tapData = commonData.getTapData();

                //当該日付に対するマーク状態を取得
                Integer state = tapData.checkDateState(mSelectedMark.getPid(), date);
                if (state != null) {
                    //対象日のデータがあれば、マークに反映
                    v_mark.setVisibility(state);
                }
            }
        }

        /*
         * セルの本日設定
         */
        public void setTodayCell( int position ){
            //Padding設定
            tv_date.setPadding(24, 1, 24, 1);
            //サークル設定
            tv_date.setBackgroundResource(R.drawable.today_circle);
        }
    }

    /*
     * コンストラクタ
     */
    public CalendarAdapter(Context context, MarkDateArrayList<MarkDateTable> markDates, MarkCountView markCountView){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDateManager = new DateManager();
        mDaysInMonth = mDateManager.getDays();
        mMarkDates   = markDates;
        mMarkCountView = markCountView;

        //本リストは、選択マーク設定時に更新する
        mShowMarkDates = new MarkDateArrayList<>();
    }

    @Override
    public int getCount() {
        //その月の日数
        Log.i("CalendarAdapter", "getCount()=" + mDaysInMonth.size());
        return mDaysInMonth.size();
    }

    /*
     * セル一つ一つを描画する際にコールされる。
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //findViewById()で取得した参照を保持するためのクラス
        ViewHolder holder;

        //初めて表示されるなら、セルを割り当て。セルはレイアウトファイルを使用。
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.calendar_day_cell, null);
            //convertView = new DateCellView(mContext);

            //ビューを生成。レイアウト内のビューを保持。
            holder = new ViewHolder();
            holder.setView( convertView );

            //タグ設定
            convertView.setTag(holder);

        } else {
            //一度表示されているなら、そのまま活用
            holder = (ViewHolder)convertView.getTag();
        }

        //１週間の日数
        final int WEEK_DAYS = 7;

        //当月の週数
        int weekNum = mDateManager.getWeeks();

        //セルのサイズを指定
        //画面解像度の比率を取得
        float dp = mContext.getResources().getDisplayMetrics().density;
        //セルの幅と高さ
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                parent.getWidth() / WEEK_DAYS - (int)dp,
                (parent.getHeight() - (int)dp * weekNum ) / weekNum);
        convertView.setLayoutParams(params);

        //Log.i("CalendarAdapter", "weekNum=" + weekNum + " param.height=" + params.height);

        //日付セルを初期化
        holder.clearData( position );

        Date date = mDaysInMonth.get(position);

        //セルの日付が当月のものである場合
        if (mDateManager.isCurrentMonth( date ) ){
            //日付情報の設定
            holder.setDateInfo( position );

            //今日の日付のみ、太字
            if(mDateManager.isCurrentDay( date )) {
                holder.setTodayCell( position );
            }
        }

        //--log
        //SimpleDateFormat sdf_d = new SimpleDateFormat("d", Locale.US);
        //Log.i("CalendarAdapter", "position=" + position + " 日=" + sdf_d.format(mDaysInMonth.get(position)));
        //--log

        //設定したビューを返す(このビューが日付セルとして表示される)
        return convertView;
    }

    //指定位置の日付を取得する
    public String getDate(int position){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.US);
        return format.format( mDaysInMonth.get(position) );
    }

    //当月を取得
    public String getMonth(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM", Locale.US);
        return format.format( mDateManager.mCalendar.getTime() );
    }

    //翌月表示
    public void nextMonth(){
        //翌月
        mDateManager.nextMonth();
        //保持する日数リストも更新する。
        mDaysInMonth = mDateManager.getDays();

        //自身へ変更通知
        this.notifyDataSetChanged();
    }

    //前月表示
    public void prevMonth(){
        mDateManager.prevMonth();
        mDaysInMonth = mDateManager.getDays();

        //自身へ変更通知
        this.notifyDataSetChanged();
    }

    /*
     * マークの設定
     */
    public void setMark( MarkTable mark ){
        //選択中マークの変更
        mSelectedMark = mark;

        //表示対象の日付マークリストを再生成
        if( mSelectedMark != null ){
            mShowMarkDates = mMarkDates.extractDesignatedMark( mSelectedMark.getPid() );
        } else {
            mShowMarkDates.clear();
        }

        //自身へ変更通知
        this.notifyDataSetChanged();
    }

    /*
     * マークの設定
     */
    public void updateMarkDate( MarkDateArrayList<MarkDateTable> markDates){
        //日付マーク更新
        mMarkDates = markDates;

        //表示対象の日付マークリストを再生成
        if( mSelectedMark != null ){
            mShowMarkDates = mMarkDates.extractDesignatedMark( mSelectedMark.getPid() );
        } else {
            mShowMarkDates.clear();
        }

        //自身へ変更通知
        this.notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }


    /*
     * 日付タッチリスナー
     */
    public class DateTouchListener implements View.OnTouchListener {

        //日付セルビュー
        private final ViewHolder viewHolder;
        //ダブルタップ検知用
        private final GestureDetector mDoubleTapDetector;

        /*
         * コンストラクタ
         */
        public DateTouchListener( ViewHolder view ) {

            //日付セルビュー
            viewHolder = view;
            //ダブルタップリスナーを実装したGestureDetector
            mDoubleTapDetector = new GestureDetector(view.tv_date.getContext(), new DoubleTapListener());
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            //ダブルタップ処理
            return mDoubleTapDetector.onTouchEvent(event);
        }

        /*
         * ダブルタップリスナー
         */
        private class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {

            /*
             * ダブルタップリスナー
             *　　マークの付与・削除を行う
             */
            @Override
            public boolean onDoubleTap(MotionEvent event) {

                Log.i("tap", "onDoubleTap");

                //日付の設定がない場合は、マーク設定対象外
                if( viewHolder.tv_date.getText().toString().isEmpty() ){
                    return true;
                }

                //共通データ
                CommonData commonData = (CommonData)((Activity) viewHolder.v_mark.getContext()).getApplication();

                //登録マークが0なら、マーク設定対象外
                if( commonData.getMarks().size() == 0 ){
                    return true;
                }

                //ダブルタップ時のマークの状態
                int preState = viewHolder.v_mark.getVisibility();
                int countValue;

                //マークの付与・削除
                if( preState == View.GONE ){
                    viewHolder.v_mark.setVisibility( View.VISIBLE );
                    countValue = MarkCountView.UP;

                } else {
                    //削除は、Invisibleではなく、Goneで行う
                    //※Invisibleだと非表示反映されないケースがあるため（
                    // 例）６行あるカレンダーの先頭の日 2022.01.30 など）
                    viewHolder.v_mark.setVisibility( View.GONE );
                    countValue = MarkCountView.DOWN;
                }

                //マーク数反映
                mMarkCountView.countUpDown(countValue);

                //保存対象データを生成
                TapData tapData = new TapData( mSelectedMark.getPid(), getDate( viewHolder.position ), viewHolder.v_mark.getVisibility() );
                //保存用キューに記録
                commonData.enqueMarkedDate( tapData );

                //振動設定
                final int VIBRATION_MILLS = 20;

                Vibrator vibrator = ((Vibrator)viewHolder.v_mark.getContext().getSystemService(VIBRATOR_SERVICE));
                if (Build.VERSION.SDK_INT < 26) {
                    //26未満
                    vibrator.vibrate(VIBRATION_MILLS);
                } else {
                    //26以上
                    VibrationEffect effect = VibrationEffect.createOneShot(VIBRATION_MILLS, VibrationEffect.DEFAULT_AMPLITUDE);
                    vibrator.vibrate(effect);
                }

                //Log.i("tap", "日付→" + getDate( viewHolder.position ) + " マーク→" + mSelectedMark.getName());

                return true;
            }
        }
    }


}
