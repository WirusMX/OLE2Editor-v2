package com.wirusmx.ole2editor.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class Converter {
    /**
     * Convert bytes array to 16 bits integer value, used ByteBuffer
     *
     * @param order - bytes order
     * @param bytes - bytes to converting. Bytes count must be 2
     * @return integer value from bytes
     * @throws IllegalArgumentException if bytes count not equal 2
     */
    public static short bytesToShort(ByteOrder order, byte... bytes) {
        if (bytes == null || bytes.length != 2) {
            throw new IllegalArgumentException("Bytes count must be 2");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 2);
        buffer.order(order);
        return buffer.getShort();
    }

    /**
     * Convert bytes array to 32 bits integer value, used ByteBuffer
     *
     * @param order - bytes order
     * @param bytes - bytes to converting. Bytes count must be 4
     * @return integer value from bytes
     * @throws IllegalArgumentException if bytes count not equal 4
     */
    public static int bytesToInt(ByteOrder order, byte... bytes) {
        if (bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("Bytes count must be 4");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 4);
        buffer.order(order);
        return buffer.getInt();
    }

    /**
     * Convert bytes array to 64 bits integer value, used ByteBuffer
     *
     * @param order - bytes order
     * @param bytes - bytes to converting. Bytes count must be 8
     * @return integer value from bytes
     * @throws IllegalArgumentException if bytes count not equal 8
     */
    public static long bytesToLong(ByteOrder order, byte... bytes) {
        if (bytes == null || bytes.length != 8) {
            throw new IllegalArgumentException("Bytes count must be 8");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 8);
        buffer.order(order);
        return buffer.getLong();
    }

    public static String utf16BytesToString(ByteOrder order, byte... bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }

        if (bytes.length % 2 != 0) {
            throw new IllegalArgumentException("Bytes count must be even");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(order);

        String result = "";
        char ch;
        int processedBytes = 0;
        while (processedBytes < bytes.length && (ch = buffer.getChar()) != 0){
            result += ch;
            processedBytes += 2;
        }

        return result;
    }

    public static byte[] stringToUtf16Bytes(String string, ByteOrder order, boolean useDefaultBufferSize) {
        if (string == null || string.length() == 0) {
            return new byte[0];
        }

        if (string.length() > 62){
            throw new IllegalArgumentException("String length can not be greater then 31");
        }

        int bufferSize = string.length() * 2;
        if (useDefaultBufferSize){
            bufferSize = 64;
        }

        ByteBuffer buffer = ByteBuffer.wrap(new byte[bufferSize]);
        buffer.order(order);
        for (char ch: string.toCharArray()){
            buffer.putChar(ch);
        }

        return buffer.array();
    }
}
