package com.klakier.proRobIntranet.api.call;

import com.klakier.proRobIntranet.api.response.StandardResponse;

public interface ApiCall {
    void enqueue(final OnResponseListener onResponseListener);

    StandardResponse execute();
}
