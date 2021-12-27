package com.mark.markcalendar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarkDateTableDao {

    @Query("SELECT * FROM mark_date")
    List<MarkDateTable> getAll();

    /*
     * 取得：レコード
     *   指定プライマリーキーのレコードを取得
     */
    @Query("SELECT * FROM mark_date WHERE pid=(:pid)")
    MarkDateTable getMark(int pid);

    @Insert
    long insert(MarkDateTable markDateTable);

    /*
     * 削除：レコード
     *   マークPid／日付を指定
     */
    @Query("SELECT * FROM mark_date WHERE pid_put_mark=(:pidPutMark) and date=(:date)")
    void deleteByDate(int pidPutMark, String date);

    @Delete
    void delete(MarkDateTable markDateTable);

    @Query("DELETE FROM mark_date")
    void deleteAll();

}
