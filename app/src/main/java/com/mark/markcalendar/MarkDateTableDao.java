package com.mark.markcalendar;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

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

    @Delete
    void delete(MarkDateTable markDateTable);

    @Query("DELETE FROM mark_date")
    void deleteAll();

}
