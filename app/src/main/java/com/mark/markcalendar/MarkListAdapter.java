package com.mark.markcalendar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MarkListAdapter extends RecyclerView.Adapter<MarkListAdapter.MarkViewHolder>{

    //マークリスト
    private final ArrayList<MarkTable> mData;

    /*
     * ViewHolder：リスト内の各アイテムのレイアウトを含む View のラッパー
     * (固有のためインナークラスで定義)
     */
    class MarkViewHolder extends RecyclerView.ViewHolder {

        private final ConstraintLayout cl_markItem;
        private final View             v_mark;
        private final TextView         tv_MarkName;

        /*
         * コンストラクタ
         */
        public MarkViewHolder(View itemView) {
            super(itemView);

            cl_markItem = itemView.findViewById(R.id.cl_markItem);
            v_mark      = itemView.findViewById(R.id.v_mark);
            tv_MarkName = itemView.findViewById(R.id.tv_markName);

        }
    }

    /*
     * コンストラクタ
     */
    public MarkListAdapter( ArrayList<MarkTable> data ) {
        mData = data;
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
    public MarkViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        //ビューを生成
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.item_mark, viewGroup, false);

        return new MarkViewHolder(view);
    }

    /*
     * ViewHolderの設定
     *   表示内容等の設定を行う
     */
    @Override
    public void onBindViewHolder(@NonNull MarkViewHolder viewHolder, final int i) {

        MarkTable mark = mData.get(i);

        //マーク名
        viewHolder.tv_MarkName.setText( mark.getName() );

        //クリックリスナー
        viewHolder.cl_markItem.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Context context = view.getContext();

                //マーク画面へ遷移
                //Intent intent = new Intent(context, MarkActivity.class);
                //intent.putExtra(ResourceManager.INTENT_ID_MarkLIST_TO_Mark, mark.getPid());

                //context.startActivity(intent);
            }
        });

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
