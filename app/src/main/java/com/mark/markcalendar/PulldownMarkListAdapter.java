package com.mark.markcalendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PulldownMarkListAdapter extends RecyclerView.Adapter<PulldownMarkListAdapter.MarkViewHolder>{

    //マークリスト
    private final MarkArrayList<MarkTable> mData;
    //アイテムクリックリスナー
    private View.OnClickListener itemClickListener;

    /*
     * ViewHolder：リスト内の各アイテムのレイアウトを含む View のラッパー
     * (固有のためインナークラスで定義)
     */
    class MarkViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout     ll_markItem;
        private final MarkView         v_mark;
        private final TextView         tv_MarkName;

        /*
         * コンストラクタ
         */
        public MarkViewHolder(View itemView) {
            super(itemView);

            ll_markItem = itemView.findViewById(R.id.ll_markItem);
            v_mark      = itemView.findViewById(R.id.v_mark);
            tv_MarkName = itemView.findViewById(R.id.tv_markName);
        }

        /*
         * ビューの設定
         */
        public void setView( MarkTable mark, int position ){
            //マーク名
            tv_MarkName.setText( mark.getName() );
            //マーク色
            v_mark.setColorHex( mark.getColor() );

            //マークリスナー
            ll_markItem.setOnClickListener( new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {

                    TextView tv_positoin = view.findViewById(R.id.tv_positoin);
                    tv_positoin.setText( Integer.toString(position) );

                    itemClickListener.onClick(view);
                }
            });

        }

    }

    /*
     * コンストラクタ
     */
    public PulldownMarkListAdapter(MarkArrayList<MarkTable> data ) {
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
        View view = inflater.inflate(R.layout.item_pulldown_mark_list, viewGroup, false);

        return new MarkViewHolder(view);
    }

    /*
     * ViewHolderの設定
     *   表示内容等の設定を行う
     */
    @Override
    public void onBindViewHolder(@NonNull MarkViewHolder viewHolder, final int position) {
        //対象マーク
        MarkTable mark = mData.get(position);

        //ビューにマーク情報を反映
        viewHolder.setView( mark, position );
    }

    /*
     * データ数取得
     */
    @Override
    public int getItemCount() {
        //表示データ数を返す
        return mData.size();
    }

    /*
     * アイテムクリックリスナーの設定
     */
    public void setOnItemClickListener( View.OnClickListener listener ){
        itemClickListener = listener;
    }

}
