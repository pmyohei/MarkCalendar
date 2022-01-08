package com.mark.markcalendar;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

/*
 * ダイアログ：「マーク」リストのプルダウン
 */
public class MarkSpinnerDialog extends DialogFragment {

    //マーク名リスト
    private ArrayList<String> mMarkNameList;
    //アイテムクリックリスナー
    private AdapterView.OnItemClickListener itemClickListener;

    /*
     * コンストラクタ
     */
    public MarkSpinnerDialog( MarkArrayList<MarkTable> marks ) {
        //マーク名リストを取得
        mMarkNameList = marks.getMarkNames();
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

        // リスト項目とListViewを対応付けるArrayAdapterを用意する
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mMarkNameList);

        // ListViewにArrayAdapterを設定する
        ListView lv_markName = dialog.findViewById(R.id.lv_markName);
        lv_markName.setAdapter(adapter);

        //アイテムクリックリスナー
        lv_markName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClickListener.onItemClick( parent, view, position, id );
                dismiss();
            }
        });
    }

    /*
     * ダイアログサイズ設定
     */
    public void setOnItemClickListener( AdapterView.OnItemClickListener listener ){
        itemClickListener = listener;
    }

}
