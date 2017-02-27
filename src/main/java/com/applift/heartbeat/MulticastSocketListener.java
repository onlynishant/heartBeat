/**
 * Created by nishantkumar on 21/01/17.
 */
package com.applift.heartbeat;

import com.applift.heartbeat.Utils.AutoExpiryMap;
import com.applift.heartbeat.Utils.ExpiryCallback;
import com.applift.heartbeat.Utils.IPUtils;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MulticastSocketListener implements Runnable {

    final static String INET_ADDR = "224.0.0.1";
    final static int PORT = 8888;
    private AutoExpiryMap autoExpiryMap = null;

    public MulticastSocketListener(AutoExpiryMap map){
        autoExpiryMap = map;
    }

    public static void main(String[] args) throws UnknownHostException, InterruptedException {
        Thread t = new Thread(new MulticastSocketListener(new AutoExpiryMap()));
        t.start();
        t.join();
    }

    class Callback implements ExpiryCallback {
        @Override
        public void onExpiry(long key, long value) {
            System.out.println("ALERT: IP expired: " + IPUtils.NumberToIP(key));
        }
    }

    @Override
    public void run() {
        // Create a buffer of bytes, which will be used to store
        // the incoming bytes containing the information from the server.
        // Since the message is small here, 256 bytes should be enough.
        byte[] buf = new byte[256];

        // Create a new Multicast socket (that will allow other sockets/programs
        // to join it as well.
        try (MulticastSocket clientSocket = new MulticastSocket(PORT)) {
            // Get the address that we are going to connect to.
            InetAddress address = InetAddress.getByName(INET_ADDR);
            //Joint the Multicast group.
            clientSocket.joinGroup(address);

            while (true) {
                // Receive the information and print it.
                DatagramPacket msgPacket = new DatagramPacket(buf, buf.length);
                clientSocket.receive(msgPacket);

                String msg = new String(buf, 0, msgPacket.getLength());
                String[] split = msg.split("@");
                long ip = Long.parseLong(split[1]);
                System.out.println("Socket 1 received msg: " + ip);
                autoExpiryMap.addEntry(ip , new Callback());
            }

            //clientSocket.leaveGroup(address);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}

