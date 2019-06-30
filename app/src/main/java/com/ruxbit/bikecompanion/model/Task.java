package com.ruxbit.bikecompanion.model;

import android.text.TextUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Parcel
@Entity(foreignKeys = @ForeignKey(entity = Part.class, parentColumns = "id", childColumns = "partId", onDelete = CASCADE))
public class Task {
    public static final int UNIT_KM = 0;
    public static final int UNIT_H = 1;

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(index = true)
    private int partId;
    private String name;
    private int units;
    private int interval;
    private float progress;
    private Date dateStart;

    @Ignore
    public Task(int id, int partId, String name, int units, int interval, Date dateStart) {
        this.id = id;
        this.partId = partId;
        this.name = name;
        this.units = units;
        this.interval = interval;
        this.dateStart = dateStart;
    }

    public Task() {

    }

    @Ignore
    @ParcelConstructor
    public Task(int id, int partId, String name, int units, int interval, float progress, Date dateStart) {
        this.id = id;
        this.partId = partId;
        this.name = name;
        this.units = units;
        this.interval = interval;
        this.progress = progress;
        this.dateStart = dateStart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Boolean isValid() {
        return (!TextUtils.isEmpty(name)) && (interval > 0) && (interval < 100000);
    }
}
