package com.mark.markcalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class MarkInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_information);

        //
        ArrayList<MarkTable> marks = new ArrayList<>();

        //レイアウトからリストビューを取得
        RecyclerView rv_markList = findViewById(R.id.rv_markList);
        //アダプタの生成
        MarkListAdapter adapter = new MarkListAdapter(marks);
        //アダプタの設定
        rv_markList.setAdapter(adapter);
        //レイアウトマネージャの設定
        rv_markList.setLayoutManager( new LinearLayoutManager(this) );

        //仮
        //クリックリスナー
        findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //マーク生成ダイアログ
/*                //-- 「やること」追加ダイアログの生成
                //Bundle生成
                Bundle bundle = new Bundle();
                //FragmentManager生成
                FragmentManager transaction = getSupportFragmentManager();

                FragmentTransaction aa = getSystemService();

                //ダイアログを生成
                DialogFragment dialog = new EntryMarkDialog();
                dialog.setArguments(bundle);
                dialog.show(transaction, "create");*/



            }
        });



    }
}