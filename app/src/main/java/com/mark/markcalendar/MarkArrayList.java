package com.mark.markcalendar;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MarkArrayList<E> extends ArrayList<MarkTable> {

    /* 定数 */
    public static final int NO_DATA = -1;               //データなし
    public static final String DELIMITER_ORDER = ",";      //マーク並び順の文字列のデリミタ


    /*
     * コンストラクタ
     */
    public MarkArrayList() {
        super();
    }

    /*
     *　ラストIndex取得
     */
    public int getLastIdx() {

        int size = size();

        if (size == 0) {
            return NO_DATA;
        }

        return size - 1;
    }

    /*
     *　指定PIDのマークを取得
     */
    public MarkTable getMark(int pid) {

        int size = size();
        for (int i = 0; i < size; i++) {

            if (pid == get(i).getPid()) {
                //指定PIDと一致するマークを返す
                return get(i);
            }
        }

        return null;
    }

    /*
     *　指定PIDのマークのindexを取得
     */
    public int getMarkIdx(int pid) {

        int size = size();
        for (int i = 0; i < size; i++) {

            if (pid == get(i).getPid()) {
                //指定PIDのindexを返す
                return i;
            }
        }

        return NO_DATA;
    }

    /*
     *　順番指定通りのマークリストを作成
     */
    public void addInOrder(MarkArrayList<MarkTable> marks, String order) {

        //並び情報がないなら、テーブル格納順でリストを作成
        if (order.equals(CalendarActivity.INVALID_MARK_ORDER)) {
            addAll(marks);
            return;
        }

        //並び情報を分解
        String[] pidArr = order.split(DELIMITER_ORDER);

        //数が一致しないなら、テーブル格納順でリストを作成
        if (marks.size() != pidArr.length) {
            addAll(marks);
            return;
        }

        for (String s : pidArr) {
            //マークを取得
            int orderPid = Integer.parseInt(s);
            MarkTable mark = marks.getMark(orderPid);

            //リスト内にないpidがあれば、テーブル格納順でリストを作成しなおして終了
            if (mark == null) {
                clear();
                addAll(marks);
                return;
            }

            //追加
            add(mark);
        }
    }

    /*
     *　本リストの並び順文字列を作成
     */
    public String getOrder() {

        StringBuilder order = new StringBuilder();

        for (MarkTable mark : this) {
            //pidを文字列に変換
            String pidStr = Integer.toString(mark.getPid());
            //pidとデリミタを連結
            order.append(pidStr).append(DELIMITER_ORDER);
        }

        return order.toString();
    }

    /*
     *　指定PIDの次のマークを取得
     *   最後の要素のマークが指定された場合は、先頭マークを返す
     */
    public MarkTable getNextMark(int pid) {

        //指定マークのIndexを取得
        int idx = getMarkIdx(pid);

        if (idx == getLastIdx()) {
            //指定マークが最後の要素なら、リスト先頭のマークを返す
            return get(0);

        } else {
            //指定マークが最後でなければ、次のマークを返す
            return get(idx + 1);
        }
    }

    /*
     *　指定PIDの前のマークを取得
     *   先頭のマークが指定された場合は、最後尾のマークを返す
     */
    public MarkTable getPreviousMark(int pid) {

        //指定マークのIndexを取得
        int idx = getMarkIdx(pid);

        if (idx == 0) {
            //指定マークが先頭なら、リスト最後のマークを返す
            return get(getLastIdx());

        } else {
            //指定マークが最後でなければ、前のマークを返す
            return get(idx - 1);
        }
    }

    /*
     * リスト内の対象のマークを、指定されたマークの情報で更新する
     */
    public int editMark(MarkTable mark) {

        //編集対象マークIndex
        int idx = getMarkIdx(mark.getPid());
        if (idx == NO_DATA) {
            return NO_DATA;
        }

        //編集対象マークを取得
        MarkTable edittedMark = get(idx);

        //情報更新
        edittedMark.setName(mark.getName());
        edittedMark.setColor(mark.getColor());

        return idx;
    }

    /*
     * リスト内のマーク名をリストにして返す
     */
    public ArrayList<String> getMarkNames() {

        ArrayList<String> markNames = new ArrayList<>();

        for( MarkTable mark: this ){
            //マーク名をリストに追加
            markNames.add( mark.getName() );
        }

        return markNames;
    }
}
