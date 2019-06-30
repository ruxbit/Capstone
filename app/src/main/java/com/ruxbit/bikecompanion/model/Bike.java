package com.ruxbit.bikecompanion.model;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
@Entity
public class Bike {
    @PrimaryKey
    @NonNull
    private String id;
    private Boolean primary;
    private int distance;
    private Bitmap image;
    private String name;
    private int type;
    private String brand_name;
    private String model_name;
    private String frame_number;
    private float weight;
    private String description;

    @ParcelConstructor
    public Bike(@NonNull String id, Boolean primary, int distance, Bitmap image, String name, int type, String brand_name, String model_name, String frame_number, float weight, String description) {
        this.id = id;
        this.primary = primary;
        this.distance = distance;
        this.image = image;
        this.name = name;
        this.type = type;
        this.brand_name = brand_name;
        this.model_name = model_name;
        this.frame_number = frame_number;
        this.weight = weight;
        this.description = description;
    }

    @Ignore
    public Bike(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Ignore
    public Bike(String name, int type, String brand_name, String model_name, String frame_number, float weight, String description) {
        this.name = name;
        this.type = type;
        this.brand_name = brand_name;
        this.model_name = model_name;
        this.frame_number = frame_number;
        this.weight = weight;
        this.description = description;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public Boolean getPrimary() {
        return primary;
    }

    public void setPrimary(Boolean primary) {
        this.primary = primary;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public String getFrame_number() {
        return frame_number;
    }

    public void setFrame_number(String frame_number) {
        this.frame_number = frame_number;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
