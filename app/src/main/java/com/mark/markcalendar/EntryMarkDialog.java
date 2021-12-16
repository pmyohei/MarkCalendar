package com.mark.markcalendar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

/*
 * ダイアログ：「マーク」生成・更新
 */
public class EntryMarkDialog extends DialogFragment {


    /*
     * コンストラクタ
     */
    public EntryMarkDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ダイアログにレイアウトを設定
        return inflater.inflate(R.layout.entry_mark_dialog, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //ダイアログ取得
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        //背景を透明にする(デフォルトテーマに付いている影などを消す) ※これをしないと、画面横サイズまで拡張されない
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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

        //サイズ設定
        //setupDialogSize(dialog);



        //「保存ボタン」のリスナー設定
        Button ib_entry = dialog.findViewById(R.id.bt_entryTask);
        ib_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //タスク名を取得
                String task = ((EditText) dialog.findViewById(R.id.et_markName)).getText().toString();



            }
        });

    }

    /*
     * ダイアログサイズ設定
     */
    private void setupDialogSize( Dialog dialog ){

        Window window= dialog.getWindow();

        //画面メトリクスの取得
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        //レイアウトパラメータ
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width   = metrics.widthPixels;
        lp.gravity = Gravity.BOTTOM;

        //サイズ設定
        window.setAttributes(lp);
    }

}
