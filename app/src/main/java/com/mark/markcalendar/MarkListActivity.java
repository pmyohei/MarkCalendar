package com.mark.markcalendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MarkListActivity extends AppCompatActivity {

    /*-- 定数 --*/
    /* 画面遷移-リクエストコード */
    public static final int REQ_CODE_CREATE = 100;
    public static final int REQ_CODE_EDIT = 101;

    /* 画面遷移-キー */
    public static String KEY_IS_CREATE = "isCreate";
    public static String KEY_MARK      = "Mark";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_information);

        //
        //ArrayList<MarkTable> marks = new ArrayList<>();

        //DB保存処理
        AsyncReadMark db = new AsyncReadMark(this, new AsyncReadMark.onFinishListener() {

            @Override
            public void onFinish(List<MarkTable> marks) {

                //レイアウトからリストビューを取得
                RecyclerView rv_markList = findViewById(R.id.rv_markList);
                //アダプタの生成
                MarkListAdapter adapter = new MarkListAdapter((ArrayList<MarkTable>) marks);
                //アダプタの設定
                rv_markList.setAdapter(adapter);
                //レイアウトマネージャの設定
                rv_markList.setLayoutManager( new LinearLayoutManager(rv_markList.getContext()) );
            }
        });
        //非同期処理開始
        db.execute();

        //マーク新規作成・編集画面遷移ランチャー
        //※クリックリスナー内で定義しないこと！（ライフサイクルの関係でエラーになるため）
        ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {

                    /*
                     * 画面遷移先からの戻り処理
                     */
                    @Override
                    public void onActivityResult(ActivityResult result) {

                        Log.i("MarkInformationActivity", "onActivityResult()");

                        //インテント
                        Intent intent = result.getData();
                        //リザルトコード
                        int resultCode = result.getResultCode();

                        //新規作成結果
                        if(resultCode == MarkEntryActivity.RESULT_CREATED) {

                            MarkTable mark = (MarkTable) intent.getSerializableExtra(MarkEntryActivity.KEY_MARK);
                            Log.i("MarkInformationActivity", "新規生成 mark=" + mark.getName() + " 色=" + mark.getColor());

                        //編集結果
                        } else if( resultCode == MarkEntryActivity.RESULT_EDITED) {


                        //その他
                        } else {
                            //do nothing
                        }
                    }
                }
        );

        //マーク新規作成リスナー
        findViewById(R.id.tv_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent生成
                Intent intent = new Intent( MarkListActivity.this, MarkEntryActivity.class );
                //渡すデータ
                intent.putExtra(KEY_IS_CREATE, true);

                //画面遷移開始
                startForResult.launch( intent );
            }
        });



    }
}