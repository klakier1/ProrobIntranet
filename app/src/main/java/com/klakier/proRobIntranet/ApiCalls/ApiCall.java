package com.klakier.proRobIntranet.ApiCalls;

public interface ApiCall {
    void enqueue(final OnResponseListener onResponseListener);
}
