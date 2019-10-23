package com.klakier.ProRobIntranet.ApiCalls;

import com.klakier.ProRobIntranet.Responses.StandardResponse;

public interface OnResponseListener {
    void onSuccess(StandardResponse response);

    void onFailure(StandardResponse response);
}
