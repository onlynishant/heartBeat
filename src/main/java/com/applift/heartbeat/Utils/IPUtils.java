package com.applift.heartbeat.Utils;

/**
 * Created by nishantkumar on 25/01/17.
 */
public class IPUtils {
    public static long IPtoNumber(String ipString) {
        int[] ip = new int[4];
        String[] parts = ipString.split("\\.");

        for (int i = 0; i < 4; i++) {
            ip[i] = Integer.parseInt(parts[i]);
        }
        long ipNumbers = 0;
        for (int i = 0; i < 4; i++) {
            ipNumbers = ipNumbers << 8 | (ip[i] & 0xFF);
        }
        return ipNumbers;
    }

    public static long IPtoNumber(byte[] ip) {
        long result = 0;
        for (byte b : ip) {
            result = result << 8 | (b & 0xFF);
        }
        return result;
    }

    public static String NumberToIP(long ip) {
        return (ip >> 24 & 0xFF) + "." + (ip >> 16 & 0xFF) + "." + (ip >> 8 & 0xFF) + "." + (ip & 0xFF);
    }

    public static void main(String[] args) {
    }

}
