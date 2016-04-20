package com.iocm.business.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/25.
 */
public class ItemData<T, V> implements Parcelable, Serializable {

    private T key;
    private V value;


    public ItemData() {
    }

    @Override
    public String toString() {
        return "ItemData{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    public ItemData(T key, V value) {
        this.key = key;
        this.value = value;
    }

    protected ItemData(Parcel in) {
    }

    public static final Creator<ItemData> CREATOR = new Creator<ItemData>() {
        @Override
        public ItemData createFromParcel(Parcel in) {
            return new ItemData(in);
        }

        @Override
        public ItemData[] newArray(int size) {
            return new ItemData[size];
        }
    };

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
