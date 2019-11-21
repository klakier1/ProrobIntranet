package com.klakier.proRobIntranet.api.call;

public interface ApiCall {
    void enqueue(final OnResponseListener onResponseListener);
}
