package com.klakier.proRobIntranet.ApiCalls;

import com.klakier.proRobIntranet.Responses.StandardResponse;

public interface OnResponseListener {
    void onSuccess(StandardResponse response);
    void onFailure(StandardResponse response);
}
