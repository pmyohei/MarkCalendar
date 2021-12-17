package com.mark.markcalendar;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/*
 * マークテーブル
 */
@Entity(tableName = "mark")
public class MarkTable implements Serializable {

    //シリアルID
    private static final long serialVersionUID = 1L;

    //主キー
    @PrimaryKey(autoGenerate = true)
    private int pid;

    //マーク名
    @ColumnInfo(name = "name")
    private String name;

    //マークカラー（ColorHex）
    @ColumnInfo(name = "color")
    private int color;

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

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

}
