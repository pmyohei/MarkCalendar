package com.mark.markcalendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MarkListAdapter extends RecyclerView.Adapter<MarkListAdapter.MarkViewHolder>{

    //マークリスト
    private final MarkArrayList<MarkTable> mData;
    //編集画面遷移ランチャー
    ActivityResultLauncher<Intent> mMarkEntryLauncher;

    /*
     * ViewHolder：リスト内の各アイテムのレイアウトを含む View のラッパー
     * (固有のためインナークラスで定義)
     */
    class MarkViewHolder extends RecyclerView.ViewHolder {

        private final MarkView         v_mark;
        private final TextView         tv_MarkName;
        private final ImageButton      ib_markInfo;
        private final ImageButton      ib_edit;
        private final ImageButton      ib_delete;

        /*
         * コンストラクタ
         */
        public MarkViewHolder(View itemView) {
            super(itemView);

            v_mark      = itemView.findViewById(R.id.v_mark);
            tv_MarkName = itemView.findViewById(R.id.tv_markName);
            ib_markInfo = itemView.findViewById(R.id.ib_markInfo);
            ib_edit     = itemView.findViewById(R.id.ib_edit);
            ib_delete   = itemView.findViewById(R.id.ib_delete);
        }

        /*
         * ビューの設定
         */
        public void setView( MarkTable mark, int position ){
            //マーク名
            tv_MarkName.setText( mark.getName() );
            //マーク色
            v_mark.setColorHex( mark.getColor() );

            //マーク情報表示リスナー
            ib_markInfo.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.i("MarkListAdapter", "ib_markInfo click()");

                    Context context = view.getContext();

                    //マーク画面へ遷移
                    //Intent intent = new Intent(context, MarkActivity.class);
                    //intent.putExtra(, mark.getPid());

                    //context.startActivity(intent);
                }
            });

            //編集リスナー
            ib_edit.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Context context = view.getContext();

                    //マーク画面へ遷移
                    Intent intent = new Intent(context, MarkEntryActivity.class);
                    intent.putExtra( MarkListActivity.KEY_MARK, mark);

                    mMarkEntryLauncher.launch( intent );
                }
            });

            //削除リスナー
            ib_delete.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //削除確認ダイアログを表示
                    new AlertDialog.Builder( view.getContext() )
                            .setTitle("マーク削除確認")
                            .setMessage("カレンダーにつけたマークも全て削除されます。\n本当に削除しますか？")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //DB削除処理
                                    AsyncDeleteMark db = new AsyncDeleteMark(view.getContext(), mark, new AsyncDeleteMark.onFinishListener() {

                                        @Override
                                        public void onFinish() {
                                            //削除マークのindexを取得
                                            int idx = mData.getMarkIdx( mark.getPid() );
                                            //自分自身をリストから削除
                                            mData.remove( idx );
                                            //アダプタに通知
                                            notifyItemRemoved( idx );
                                        }
                                    });
                                    //非同期処理開始
                                    db.execute();
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
            });
        }

    }

    /*
     * コンストラクタ
     */
    public MarkListAdapter( MarkArrayList<MarkTable> data, ActivityResultLauncher<Intent> markEntryLauncher ) {
        mData = data;
        mMarkEntryLauncher = markEntryLauncher;
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



}
