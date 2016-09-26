package com.wirusmx.ole2editor.utils;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConverterTest extends Assert {

    private static final ByteOrder BYTE_ORDER = ByteOrder.BIG_ENDIAN;

    @Test
    public void bytesToShortConverterTest() {
        short[] testNumbers = new short[]{-5555, -555, -2, -1, 0, 1, 2, 555, 5555};

        for (short i: testNumbers){
            assertEquals("",
                    i,
                    Converter.bytesToShort(BYTE_ORDER, ByteBuffer.allocate(2).putShort(i).array()));
        }
    }

    @Test
    public void bytesToIntConverterTest() {
        int[] testNumbers = new int[]{-55555555, -5555555, -555555, -5555, -555, -2, -1,
        0, 1, 2, 555, 5555, 555555, 5555555, 55555555, 555555555};

        for (int i: testNumbers){
            assertEquals("",
                    i,
                    Converter.bytesToInt(BYTE_ORDER, ByteBuffer.allocate(4).putInt(i).array()));
        }
    }

    @Test
    public void bytesToLongConverterTest() {
        long[] testNumbers = new long[]{Long.MIN_VALUE, -555555555, -55555555, -5555555, -555555, -5555, -555, -2, -1,
                0, 1, 2, 555, 5555, 555555, 5555555, 55555555, 555555555, Long.MAX_VALUE};

        for (long i: testNumbers){
            assertEquals("",
                    i,
                    Converter.bytesToLong(BYTE_ORDER, ByteBuffer.allocate(8).putLong(i).array()));
        }
    }
}
