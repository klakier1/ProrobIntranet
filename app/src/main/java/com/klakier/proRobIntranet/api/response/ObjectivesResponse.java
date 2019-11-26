package com.klakier.proRobIntranet.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ObjectivesResponse extends StandardResponse {

    @SerializedName("data_length")
    @Expose
    private Integer dataLength;
    @SerializedName("data")
    @Expose
    private List<String> data = null;

    /**
     * No args constructor for use in serialization
     */
    public ObjectivesResponse() {
    }

    /**
     * @param message
     * @param error
     * @param data
     * @param dataLength
     */
    public ObjectivesResponse(Boolean error, String message, Integer dataLength, List<String> data) {
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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

}

