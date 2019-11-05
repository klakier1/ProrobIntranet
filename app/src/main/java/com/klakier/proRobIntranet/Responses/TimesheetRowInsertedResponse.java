package com.klakier.proRobIntranet.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TimesheetRowInsertedResponse extends StandardResponse {

    @SerializedName("id")
    @Expose
    private Integer id;

    /**
     * No args constructor for use in serialization
     */
    public TimesheetRowInsertedResponse() {
    }

    /**
     * @param message
     * @param error
     * @param id
     */
    public TimesheetRowInsertedResponse(Boolean error, String message, Integer id) {
        super(error, message);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer dataLength) {
        this.id = id;
    }

}

