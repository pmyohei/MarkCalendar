package com.mark.markcalendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
 * マークテーブル
 */
@Entity(tableName = "mark")
public class MarkTable {

    //主キー
    @PrimaryKey(autoGenerate = true)
    private int pid;

    //マーク名
    @ColumnInfo(name = "name")
    private String name;

    //マークカラー（ColorHex）
    @ColumnInfo(name = "color")
    private long color;

    public int getPid() {
        return pid;
    }
    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public long getColor() {
        return color;
    }
    public void setColor(long color) {
        this.color = color;
    }

}
