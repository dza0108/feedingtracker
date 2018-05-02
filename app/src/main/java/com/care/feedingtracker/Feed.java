package com.care.feedingtracker;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName = "feed_table")
public class Feed {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "milk")
    private int milk_volume;

    @ColumnInfo(name = "solid")
    private String solid_name;

    @ColumnInfo(name = "diaper")
    private String diaper;

    public Feed(){
        timestamp = 0;
    }

    public Feed(@NonNull long t, int milk, String solid, String d){
        this.timestamp = t/1000/60;
        this.milk_volume = milk;
        this.solid_name = solid;
        this.diaper = d;
    }

    public void setTimestamp(@NonNull long timestamp) {
        this.timestamp = timestamp/1000/60;
    }

    public void setDiaper(String diaper) {
        this.diaper = diaper;
    }

    public void setMilk_volume(int milk) {
        this.milk_volume = milk;
    }

    public void setSolid_name(String solid) {this.solid_name = solid; }

    @NonNull
    public long getTimestamp() {
        return this.timestamp*1000*60;
    }

    public int getMilk_volume() {
        return this.milk_volume;
    }

    public String getSolid_name() {
        return solid_name;
    }

    public String getDiaper() {
        return this.diaper;
    }

    @Override
    public String toString() {
        return "Time: " + timestamp*1000*60 + " food: " + milk_volume + " " + solid_name + " diaper: " + diaper;
    }

    public String toFormattedString() {
        Date t = new Date();
        t.setTime(this.getTimestamp());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm a");

        StringBuilder b = new StringBuilder("Time: ");
        b.append(formatter.format(t));

        if (this.getMilk_volume() != 0) {
            b.append("\n -> Milk: " + this.getMilk_volume() + "ml; ");
        } else if (!this.getSolid_name().isEmpty()) {
            b.append("\n -> Solid: " + this.getSolid_name() + "; ");
        } else {
            b.append("\n -> NO FOOD; ");
        }

        if (!this.getDiaper().matches("None")) {
            b.append("Diaper: " + this.getDiaper());
        }
        return b.toString();
    }
}
