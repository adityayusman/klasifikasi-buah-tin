package com.mysankato.klasifikasikematanganbuahtin.DataModel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "hasilKlasifikasi")
public class HasilKlasifikasi implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "result")
    private String result;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] figImage;

    @ColumnInfo(name = "time")
    private String time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public byte[] getFigImage() {
        return figImage;
    }

    public void setFigImage(byte[] figImage) {
        this.figImage = figImage;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
