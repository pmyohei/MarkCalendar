package com.mark.markcalendar;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/*
 * Database定義
 */
@Database(
        entities = {
                MarkTable.class,            //マークテーブル
                MarkDateTable.class,        //マーク日テーブル
        },
        version = 1,
        exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    //DAO
    public abstract MarkTableDao        daoMarkTable();               //マークテーブル
    public abstract MarkDateTableDao    daoMarkDateTable();              //マーク日テーブル

}