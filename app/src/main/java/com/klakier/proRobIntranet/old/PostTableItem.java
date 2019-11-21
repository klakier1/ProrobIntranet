package com.klakier.proRobIntranet.old;

public class PostTableItem
{
    public PostTableItem(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private String key;
    private String value;
}
