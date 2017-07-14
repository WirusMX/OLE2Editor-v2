package com.wirusmx.ole2editor.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class Converter {
    /**
     * Converts bytes array to 16 bits integer value, used <code>{@link ByteBuffer}</code>.
     *
     * @param order - bytes order.
     * @param bytes - bytes to converting. Bytes count must be 2.
     * @return integer value from bytes.
     * @throws IllegalArgumentException if bytes count not equal 2.
     */
    public static short bytesToInt16(ByteOrder order, byte... bytes) {
        if (bytes == null || bytes.length != 2) {
            throw new IllegalArgumentException("Bytes count must be 2");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 2);
        buffer.order(order);
        return buffer.getShort();

    }

    /**
     * Converts 16 bits integer value to bytes array, used <code>{@link ByteBuffer}</code>.
     *
     * @param value - value for converting.
     * @param order - bytes order.
     * @return array of bytes.
     */
    public static byte[] int16ToBytes(short value, ByteOrder order) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.order(order);
        buffer.putShort(value);
        return buffer.array();
    }

    /**
     * Convert bytes array to 32 bits integer value, used <code>{@link ByteBuffer}</code>.
     *
     * @param order - bytes order.
     * @param bytes - bytes to converting. Bytes count must be 4.
     * @return integer value from bytes.
     * @throws IllegalArgumentException if bytes count not equal 4.
     */
    public static int bytesToInt32(ByteOrder order, byte... bytes) {
        if (bytes == null || bytes.length != 4) {
            throw new IllegalArgumentException("Bytes count must be 4");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 4);
        buffer.order(order);
        return buffer.getInt();
    }

    /**
     * Converts 32 bits integer value to bytes array, used <code>{@link ByteBuffer}</code>.
     *
     * @param value - value for converting.
     * @param order - bytes order.
     * @return array of bytes.
     */
    public static byte[] int32ToBytes(int value, ByteOrder order) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(order);
        buffer.putInt(value);
        return buffer.array();
    }

    /**
     * Convert bytes array to 64 bits integer value, used <code>{@link ByteBuffer}</code>.
     *
     * @param order - bytes order
     * @param bytes - bytes to converting. Bytes count must be 8
     * @return integer value from bytes
     * @throws IllegalArgumentException if bytes count not equal 8
     */
    public static long bytesToInt64(ByteOrder order, byte... bytes) {
        if (bytes == null || bytes.length != 8) {
            throw new IllegalArgumentException("Bytes count must be 8");
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes, 0, 8);
        buffer.order(order);
        return buffer.getLong();
    }

    /**
     * Converts 64 bits integer value to bytes array, used <code>{@link ByteBuffer}</code>.
     *
     * @param value - value for converting.
     * @param order - bytes order.
     * @return array of bytes.
     */
    public static byte[] int64ToBytes(long value, ByteOrder order) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(order);
        buffer.putLong(value);
        return buffer.array();
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
        int processedBytesCount = 0;
        while (processedBytesCount < bytes.length && (ch = buffer.getChar()) != 0) {
            if (ch < 32) {
                result += String.format("[%02X]", ch & 0xFF);
            } else {
                result += ch;
            }
            processedBytesCount += 2;
        }

        return result;
    }

    public static byte[] stringToUtf16Bytes(String string, ByteOrder order) {
        return stringToUtf16Bytes(string, order, false);
    }

    public static byte[] stringToUtf16Bytes(String string, ByteOrder order, boolean useDefaultBufferSize) {
        if (string == null || string.length() == 0) {
            return new byte[0];
        }

        if (useDefaultBufferSize && string.length() > 31) {
            throw new IllegalArgumentException("String length can not be greater then 31");
        }

        int bufferSize = string.length() * 2;
        if (useDefaultBufferSize) {
            bufferSize = 64;
        }

        ByteBuffer buffer = ByteBuffer.wrap(new byte[bufferSize]);
        buffer.order(order);
        for (char ch : string.toCharArray()) {
            buffer.putChar(ch);
        }

        return buffer.array();
    }
}
