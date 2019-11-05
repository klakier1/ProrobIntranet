package com.klakier.proRobIntranet.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDataShortResponse extends StandardResponse {

    @SerializedName("data_length")
    @Expose
    private Integer dataLength;
    @SerializedName("data")
    @Expose
    private List<UserDataShort> data = null;

    /**
     * No args constructor for use in serialization
     */
    public UserDataShortResponse() {
    }

    /**
     * @param message
     * @param error
     * @param data
     * @param dataLength
     */
    public UserDataShortResponse(Boolean error, String message, Integer dataLength, List<UserDataShort> data) {
        super(error, message);
        this.dataLength = dataLength;
        this.data = data;
    }

    public Integer getDataLength() {
        return dataLength;
    }

    public void setDataLength(Integer dataLength) {
        this.dataLength = dataLength;
    }

    public List<UserDataShort> getData() {
        return data;
    }

    public void setData(List<UserDataShort> data) {
        this.data = data;
    }

}