package com.ruxbit.bikecompanion.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import static androidx.room.ForeignKey.CASCADE;

@Parcel
@Entity (foreignKeys = @ForeignKey(entity = Bike.class, parentColumns = "id", childColumns = "bikeId", onDelete = CASCADE))
public class Part {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @ColumnInfo(index = true)
    private String bikeId;
    private int type;
    private String manufacturer;
    private String model;

    @ParcelConstructor
    public Part(int id, String bikeId, int type, String manufacturer, String model) {
        this.id = id;
        this.bikeId = bikeId;
        this.type = type;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    @Ignore
    public Part(String bikeId, int type, String manufacturer, String model) {
        this.bikeId = bikeId;
        this.type = type;
        this.manufacturer = manufacturer;
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBikeId() {
        return bikeId;
    }

    public void setBikeId(String bikeId) {
        this.bikeId = bikeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
