package com.mark.markcalendar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarkTableDao {

    @Query("SELECT * FROM mark")
    List<MarkTable> getAll();

    /*
     * 取得：レコード
     *   指定プライマリーキーのレコードを取得
     */
    @Query("SELECT * FROM mark WHERE pid=(:pid)")
    MarkTable getMark(int pid);

    /*
     * 取得：レコード
     *   指定マーク名のレコードを取得
     */
    @Query("SELECT * FROM mark WHERE name=(:name)")
    MarkTable getMark(String name);

    @Insert
    long insert(MarkTable MarkTable);

    @Delete
    void delete(MarkTable MarkTable);

    @Query("DELETE FROM mark")
    void deleteAll();

}
