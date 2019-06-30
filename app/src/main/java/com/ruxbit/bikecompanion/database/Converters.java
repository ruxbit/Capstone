package com.ruxbit.bikecompanion.database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.TypeConverter;

import java.io.ByteArrayOutputStream;
import java.util.Date;

public class Converters {
    private static final int MAX_IMAGE_SIZE = 4000000;

    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    /**
     * Downscales and compresses the image
     * @param bitmap image to compress
     * @return result String
     */
    @TypeConverter
    public String bitmapToString(Bitmap bitmap) {
        if (bitmap == null)
            return null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        float scale = 1;

        if (height * width > MAX_IMAGE_SIZE) {
            scale = MAX_IMAGE_SIZE / (float)height / width;
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(width * scale), Math.round(height * scale), true);
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        scaledBitmap.recycle();
        bitmap.recycle();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    @TypeConverter
    public Bitmap stringToBitmap(String string) {
        if (string == null)
            return null;
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}