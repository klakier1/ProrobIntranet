package com.klakier.ProRobIntranet.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDataResponse extends StandardResponse {

    @SerializedName("data_length")
    @Expose
    private Integer dataLength;
    @SerializedName("data")
    @Expose
    private List<UserData> data = null;

    /**
     * No args constructor for use in serialization
     */
    public UserDataResponse() {
    }

    /**
     * @param message
     * @param error
     * @param data
     * @param dataLength
     */
    public UserDataResponse(Boolean error, String message, Integer dataLength, List<UserData> data) {
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

    public List<UserData> getData() {
        return data;
    }

    public void setData(List<UserData> data) {
        this.data = data;
    }

}