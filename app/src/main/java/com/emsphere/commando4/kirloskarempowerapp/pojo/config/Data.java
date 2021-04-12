package com.emsphere.commando4.kirloskarempowerapp.pojo.config;

import com.google.gson.annotations.SerializedName;

/**
 * Created by commando4 on 5/15/2018.
 */

public class Data {
    @SerializedName("Key")
    String Key;
    @SerializedName("Value")
    String Value;

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
