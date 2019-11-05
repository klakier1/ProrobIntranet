package com.klakier.proRobIntranet.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TimesheetResponse extends StandardResponse {

    @SerializedName("data_length")
    @Expose
    private Integer dataLength;
    @SerializedName("data")
    @Expose
    private List<TimesheetRow> data = null;

    /**
     * No args constructor for use in serialization
     */
    public TimesheetResponse() {
    }

    /**
     * @param message
     * @param error
     * @param data
     * @param dataLength
     */
    public TimesheetResponse(Boolean error, String message, Integer dataLength, List<TimesheetRow> data) {
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

    public List<TimesheetRow> getData() {
        return data;
    }

    public void setData(List<TimesheetRow> data) {
        this.data = data;
    }

}

