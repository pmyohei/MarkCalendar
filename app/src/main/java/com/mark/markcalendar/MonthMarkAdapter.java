package com.mark.markcalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MonthMarkAdapter extends RecyclerView.Adapter<MonthMarkAdapter.MonthViewHolder>{

    //月ごとマーク情報リスト
    private final MonthMarkArrayList<MonthMarkInformation> mData;
    //マーク数合計
    private final int mTotalNum;

    /*
     * ViewHolder：リスト内の各アイテムのレイアウトを含む View のラッパー
     * (固有のためインナークラスで定義)
     */
    class MonthViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_month;
        private final TextView tv_markNum;
        private final TextView tv_markRatio;

        /*
         * コンストラクタ
         */
        public MonthViewHolder(View itemView) {
            super(itemView);

            tv_month     = itemView.findViewById(R.id.tv_month);
            tv_markNum   = itemView.findViewById(R.id.tv_markNum);
            tv_markRatio = itemView.findViewById(R.id.tv_markRatio);
        }

        /*
         * ビューの設定
         */
        public void setView( MonthMarkInformation month ){
            //月の表示情報を設定
            tv_month.setText( month.getYearMonth() );

            //月のマーク数
            int monthNum = month.getMarkNum();
            tv_markNum.setText( Integer.toString(monthNum) );

            //月のマーク数の割合
            int ratio = (int)(((float)monthNum / (float)mTotalNum) * 100f);
            tv_markRatio.setText( Integer.toString(ratio) );
        }
    }

    /*
     * コンストラクタ
     */
    public MonthMarkAdapter( MonthMarkArrayList<MonthMarkInformation> data, int total ) {
        mData = data;
        mTotalNum = total;
    }

    /*
     * ここの戻り値が、onCreateViewHolder()の第２引数になる
     */
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    /*
     *　ViewHolderの生成
     */
    @NonNull
    @Override
    public MonthViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        //ビューを生成
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_month_mark, viewGroup, false);

        return new MonthViewHolder(view);
    }

    /*
     * ViewHolderの設定
     *   表示内容等の設定を行う
     */
    @Override
    public void onBindViewHolder(@NonNull MonthViewHolder viewHolder, final int position) {
        //対象マーク
        MonthMarkInformation monthData = mData.get(position);

        //ビューに月ごとのマーク情報を反映
        viewHolder.setView( monthData );
    }

    /*
     * データ数取得
     */
    @Override
    public int getItemCount() {
        //表示データ数を返す
        return mData.size();
    }



}
