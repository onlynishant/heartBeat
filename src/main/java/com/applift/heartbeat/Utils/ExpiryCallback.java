package com.applift.heartbeat.Utils;

/**
 * Created by nishantkumar on 25/01/17.
 */
public interface ExpiryCallback {
    void onExpiry(long key, long value);
}