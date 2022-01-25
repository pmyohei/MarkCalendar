package com.mark.markcalendar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*
 * ダイアログ：「マーク」リストのプルダウン
 */
public class MarkSpinnerDialog extends DialogFragment {

    //マークリスト
    private final MarkArrayList<MarkTable> mMarks;
    //アイテムクリックリスナー
    private View.OnClickListener itemClickListener;

    /*
     * コンストラクタ
     */
    public MarkSpinnerDialog( MarkArrayList<MarkTable> marks ) {
        //マーク名リストを取得
        mMarks = marks;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ダイアログにレイアウトを設定
        return inflater.inflate(R.layout.mark_spinner_dialog, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //ダイアログ取得
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //背景を透明にする(デフォルトテーマに付いている影などを消す) ※これをしないと、画面横サイズまで拡張されない
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //ダイアログを返す
        return dialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        //ダイアログ取得
        Dialog dialog = getDialog();
        if( dialog == null ){
            return;
        }

        //マークリストを表示
        RecyclerView rv_markList = dialog.findViewById(R.id.rv_mark);
        PulldownMarkListAdapter adapter = new PulldownMarkListAdapter( mMarks );
        rv_markList.setAdapter(adapter);
        rv_markList.setLayoutManager( new LinearLayoutManager(rv_markList.getContext()) );

        //アイテムクリックリスナー
        adapter.setOnItemClickListener(itemClickListener);
    }

    /*
     * アイテムクリックリスナーの設定
     */
    public void setOnItemClickListener( View.OnClickListener listener ){
        itemClickListener = listener;
    }

}
