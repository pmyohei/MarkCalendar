package com.mark.markcalendar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarkStatisticsAdapter extends RecyclerView.Adapter<MarkStatisticsAdapter.MonthViewHolder>{

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
        private final TextView tv_padding;

        /*
         * コンストラクタ
         */
        public MonthViewHolder(View itemView) {
            super(itemView);

            tv_month     = itemView.findViewById(R.id.tv_month);
            tv_markNum   = itemView.findViewById(R.id.tv_markNum);
            tv_markRatio = itemView.findViewById(R.id.tv_markRatio);
            tv_padding   = itemView.findViewById(R.id.tv_padding);
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

            //月のマーク数の割合（小数点第一位まで表示）
            float ratio = ((float)monthNum / (float)mTotalNum) * 100f;
            ratio = Math.round( ratio * 10 ) / 10f;
            tv_markRatio.setText( String.valueOf( ratio ) );

            //表示位置の調整（前方に見えない文字を置くことで、割合が揃っているように見せる）
            if( ratio < 10 ){
                //1桁なら、数値2桁分を表示
                tv_padding.setText("00");
                tv_padding.setVisibility(View.INVISIBLE);
            } else if( ratio < 100 ) {
                //2桁なら、数値1桁分を表示
                tv_padding.setText("0");
                tv_padding.setVisibility(View.INVISIBLE);
            }
        }
    }

    /*
     * コンストラクタ
     */
    public MarkStatisticsAdapter(MonthMarkArrayList<MonthMarkInformation> data, int total ) {
        mData     = data;
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
