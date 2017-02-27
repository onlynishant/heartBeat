package com.applift.heartbeat.Utils;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nishantkumar on 24/01/17.
 */
public class AutoExpiryMap {
    //Map of IP and timestamp in millis
    private ConcurrentHashMap<Long, Long> ipToTimestamp = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Long, ExpiryCallback> ipToCallback = new ConcurrentHashMap<>();

    private long EXPIRY_TIME_MILLIS = 5000;
    private Thread expiryThread = null;
    private final static long THREAD_SLEEP_MILLIS = 1000;

    public AutoExpiryMap() {
        expiryThread = new Thread(new ExpiryWorker());
        expiryThread.start();
    }

    public AutoExpiryMap(long expiryTime) {
        this();
        EXPIRY_TIME_MILLIS = expiryTime;
    }

    class ExpiryWorker implements Runnable {
        @Override
        public void run() {
            while (true) {
                if (ipToTimestamp.isEmpty()) {
                    try {
                        Thread.sleep(THREAD_SLEEP_MILLIS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                long currentUnixTimeMillis = 0;
                ExpiryCallback callback = null;
                for (Iterator<Map.Entry<Long, Long>> it = ipToTimestamp.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Long, Long> entry = it.next();
                    currentUnixTimeMillis = System.currentTimeMillis();
                    long key = entry.getKey();
                    long value = entry.getValue();
                    callback = null;
                    if (currentUnixTimeMillis - value > EXPIRY_TIME_MILLIS) {
                        //System.out.println("ALERT: key expired: " + key + " " + value);
                        it.remove();
                        callback = ipToCallback.get(key);
                        ipToCallback.remove(key, callback);
                        if (callback != null)
                            callback.onExpiry(key, value);
                    }
                }
            }
        }
    }

    public void addEntry(long key, long timeout, ExpiryCallback callback) {
        ipToTimestamp.put(key, timeout);
        if (callback == null)
            callback = new ExpiryCallback() {
                @Override
                public void onExpiry(long key, long value) {
                    System.out.println("DEFAULT CALLBACK: Expired " + key);
                }
            };
        ipToCallback.put(key, callback);
    }

    public void addEntry(long key, long timeout) {
        addEntry(key, timeout, null);
    }

    public void addEntry(long key, ExpiryCallback callback) {
        addEntry(key, System.currentTimeMillis(), callback);
    }

    public void addEntry(long key) {
        addEntry(key, System.currentTimeMillis(), null);
    }

    void removeEntry(long key) {
        ipToTimestamp.remove(key);
    }
}
