package com.mark.markcalendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/*
 * マーク日テーブル
 */
@Entity(tableName = "mark_date",
        foreignKeys = { @ForeignKey
                (entity = MarkTable.class,                  //外部キー対象とする主キーのテーブル
                        parentColumns = "pid",              //外部キー対象とする主キー
                        childColumns  = "pid_put_mark",     //外部キーとする列
                        onDelete = ForeignKey.CASCADE)},    //自動削除あり
        indices = { @Index
                (value = {"pid_put_mark"})}
)
public class MarkDateTable {

    //主キー
    @PrimaryKey(autoGenerate = true)
    private int pid;

    //外部キー：付与マーク
    @ColumnInfo(name = "pid_put_mark")
    private String pidPutMark;

    //日付
    @ColumnInfo(name = "date")
    private String date;


    /*-- getter/setter --*/
    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPidPutMark() {
        return pidPutMark;
    }
    public void setPidPutMark(String pidPutMark) {
        this.pidPutMark = pidPutMark;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

}
