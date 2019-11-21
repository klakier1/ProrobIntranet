package com.klakier.proRobIntranet.api.call;

import com.klakier.proRobIntranet.api.response.StandardResponse;

public interface OnResponseListener {
    void onSuccess(StandardResponse response);
    void onFailure(StandardResponse response);
}
