/**
 * Created by nishantkumar on 21/01/17.
 */
package com.applift.heartbeat;
import com.applift.heartbeat.Utils.IPUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MulticastSocketEmitter  implements Runnable  {

    final static String INET_ADDR = "localhost";
    final static int PORT = 9005;
    final static long HEARTBEAT_INTERVAL_MILLIS = 1000;
    private String MACHINE_ID;
    static volatile boolean STOP_HEARTBEAT = false;

    public MulticastSocketEmitter() throws UnknownHostException{
        long ipToNumber = IPUtils.IPtoNumber(InetAddress.getLocalHost().getAddress());
        MACHINE_ID = InetAddress.getLocalHost().getHostName() + "@" + ipToNumber;
    }

    public static void main(String[] args) throws InterruptedException {
        // Get the address that we are going to connect to.
        Thread t = null;
        try {
            t = new Thread(new MulticastSocketEmitter());
            t.start();
            t.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Open a new DatagramSocket, which will be used to send the data.
        System.out.println("Starting sending HeartBeat signal");
        try (DatagramSocket serverSocket = new DatagramSocket()) {
            while (!STOP_HEARTBEAT) {
                String msg = "MACHINE_ID:" + MACHINE_ID;

                // Create a packet that will contain the data
                // (in the form of bytes) and send it.
                InetAddress addr = InetAddress.getByName(INET_ADDR);
                DatagramPacket msgPacket = new DatagramPacket(msg.getBytes(),
                        msg.getBytes().length, addr, PORT);
                serverSocket.send(msgPacket);

                System.out.println("Sending HeartBeat packet with msg: " + msg);
                Thread.sleep(HEARTBEAT_INTERVAL_MILLIS);
            }
            System.out.println("Stopped sending HeartBeat signal");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
