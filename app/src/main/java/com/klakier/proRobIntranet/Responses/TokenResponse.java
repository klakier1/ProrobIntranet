package com.klakier.proRobIntranet.Responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponse extends StandardResponse{

    @SerializedName("token")
    @Expose
    private String token;

    /**
     * No args constructor for use in serialization
     *
     */
    public TokenResponse() {
        super();
    }

    /**
     *
     * @param token
     */
    public TokenResponse(Boolean error, String message, String token) {
        super(error, message);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}