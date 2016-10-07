package com.wirusmx.ole2editor.utils;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConverterTest extends Assert {

    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    @Test
    public void bytesToShortConverterTest() {
        short[] testNumbers = new short[]{-5555, -555, -2, -1, 0, 1, 2, 555, 5555};

        for (short i: testNumbers){
            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.order(BYTE_ORDER);
            buffer.putShort(i);
            assertEquals("",
                    i,
                    Converter.bytesToShort(BYTE_ORDER, buffer.array()));
        }
    }

    @Test
    public void bytesToIntConverterTest() {
        int[] testNumbers = new int[]{-55555555, -5555555, -555555, -5555, -555, -2, -1,
        0, 1, 2, 555, 5555, 555555, 5555555, 55555555, 555555555};

        for (int i: testNumbers){
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.order(BYTE_ORDER);
            buffer.putInt(i);
            assertEquals("",
                    i,
                    Converter.bytesToInt(BYTE_ORDER, buffer.array()));
        }
    }

    @Test
    public void bytesToLongConverterTest() {
        long[] testNumbers = new long[]{Long.MIN_VALUE, -555555555, -55555555, -5555555, -555555, -5555, -555, -2, -1,
                0, 1, 2, 555, 5555, 555555, 5555555, 55555555, 555555555, Long.MAX_VALUE};

        for (long i: testNumbers){
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.order(BYTE_ORDER);
            buffer.putLong(i);
            assertEquals("",
                    i,
                    Converter.bytesToLong(BYTE_ORDER, buffer.array()));
        }
    }

    @Test
    public void utf16BytesToStringTest(){
        String testString = "qwertyuiopasdfghjklzxcvbnm";
        byte[] testBytes = new byte[testString.length() * 2];
        ByteBuffer buffer = ByteBuffer.wrap(testBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (char ch: testString.toCharArray()){
            buffer.putChar(ch);
        }
        assertEquals(testString,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer.array()));

        String testString2 = "йцукенгшщзхъфывапролджэячсмитьбю";
        byte[] testBytes2 = new byte[testString2.length() * 2];
        ByteBuffer buffer2 = ByteBuffer.wrap(testBytes2);
        buffer2.order(ByteOrder.LITTLE_ENDIAN);
        for (char ch: testString2.toCharArray()){
            buffer2.putChar(ch);
        }
        assertEquals(testString2,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer2.array()));

        String testString3 = "Data";
        byte[] testBytes3 = new byte[]{68, 0, 97, 0, 116, 0, 97, 0, 0, 0, 0, 0};
        ByteBuffer buffer3 = ByteBuffer.wrap(testBytes3);
        buffer3.order(BYTE_ORDER);
        assertEquals(testString3,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer3.array()));

        String testString4 = "";
        byte[] testBytes4 = new byte[]{0, 0};
        ByteBuffer buffer4 = ByteBuffer.wrap(testBytes4);
        buffer4.order(BYTE_ORDER);
        assertEquals(testString4,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer4.array()));
    }


}
