package com.applift.heartbeat;

import com.applift.heartbeat.Utils.AutoExpiryMap;

import java.net.UnknownHostException;

/**
 * Created by nishantkumar on 21/01/17.
 */
public class Server {
    static Thread emitterThread = null;
    static Thread listnerThread = null;
    final static AutoExpiryMap autoExpiryMap = new AutoExpiryMap();

    public static void main(String[] args) {

//        if (args.length > 0) {
//            new PropertyLoader(args[0]);
//        } else {
//            new PropertyLoader();
//        }
        try {
            emitterThread = new Thread(new MulticastSocketEmitter());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        listnerThread = new Thread(new MulticastSocketListener(autoExpiryMap));

        // Starting heartbeat process
        emitterThread.start();
        listnerThread.start();
        try {
            emitterThread.join();
            listnerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
