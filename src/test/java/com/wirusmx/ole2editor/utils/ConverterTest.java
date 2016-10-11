package com.wirusmx.ole2editor.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConverterTest extends Assert {

    private static final ByteOrder BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    @Test
    public void bytesToInt16ConverterTest() {
        short[] testNumbers = new short[]{-5555, -555, -2, -1, 0, 1, 2, 555, 5555};

        for (short i : testNumbers) {
            ByteBuffer buffer = ByteBuffer.allocate(2);
            buffer.order(BYTE_ORDER);
            buffer.putShort(i);
            assertEquals("",
                    i,
                    Converter.bytesToInt16(BYTE_ORDER, buffer.array()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt16ConverterExceptionTest1() {
        Converter.bytesToInt16(BYTE_ORDER, new byte[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt16ConverterExceptionTest2() {
        Converter.bytesToInt16(BYTE_ORDER, new byte[3]);
    }

    @Test
    public void int16ToBytesTest() {
        byte[] expectedBytes = new BigInteger("" + Short.MAX_VALUE).toByteArray();
        byte[] actualBytes = Converter.int16ToBytes(Short.MAX_VALUE, ByteOrder.BIG_ENDIAN);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void bytesToInt32ConverterTest() {
        int[] testNumbers = new int[]{-55555555, -5555555, -555555, -5555, -555, -2, -1,
                0, 1, 2, 555, 5555, 555555, 5555555, 55555555, 555555555};

        for (int i : testNumbers) {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.order(BYTE_ORDER);
            buffer.putInt(i);
            assertEquals("",
                    i,
                    Converter.bytesToInt32(BYTE_ORDER, buffer.array()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt32ConverterExceptionTest1() {
        Converter.bytesToInt32(BYTE_ORDER, new byte[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt32ConverterExceptionTest2() {
        Converter.bytesToInt32(BYTE_ORDER, new byte[5]);
    }

    @Test
    public void int32ToBytesTest() {
        byte[] expectedBytes = new BigInteger("" + Integer.MAX_VALUE).toByteArray();
        byte[] actualBytes = Converter.int32ToBytes(Integer.MAX_VALUE, ByteOrder.BIG_ENDIAN);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void bytesToInt64ConverterTest() {
        long[] testNumbers = new long[]{Long.MIN_VALUE, -555555555, -55555555, -5555555, -555555, -5555, -555, -2, -1,
                0, 1, 2, 555, 5555, 555555, 5555555, 55555555, 555555555, Long.MAX_VALUE};

        for (long i : testNumbers) {
            ByteBuffer buffer = ByteBuffer.allocate(8);
            buffer.order(BYTE_ORDER);
            buffer.putLong(i);
            assertEquals("",
                    i,
                    Converter.bytesToInt64(BYTE_ORDER, buffer.array()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt64ConverterExceptionTest1() {
        Converter.bytesToInt64(BYTE_ORDER, new byte[7]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt64ConverterExceptionTest2() {
        Converter.bytesToInt64(BYTE_ORDER, new byte[9]);
    }

    @Test
    public void int64ToBytesTest() {
        byte[] expectedBytes = new BigInteger("" + Long.MAX_VALUE).toByteArray();
        byte[] actualBytes = Converter.int64ToBytes(Long.MAX_VALUE, ByteOrder.BIG_ENDIAN);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void utf16BytesToStringTest() {
        String testString = "qwertyuiopasdfghjklzxcvbnm";
        byte[] testBytes = new byte[testString.length() * 2];
        ByteBuffer buffer = ByteBuffer.wrap(testBytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for (char ch : testString.toCharArray()) {
            buffer.putChar(ch);
        }
        assertEquals("English letters", testString,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer.array()));

        String testString2 = "йцукенгшщзхъфывапролджэячсмитьбю";
        byte[] testBytes2 = new byte[testString2.length() * 2];
        ByteBuffer buffer2 = ByteBuffer.wrap(testBytes2);
        buffer2.order(ByteOrder.LITTLE_ENDIAN);
        for (char ch : testString2.toCharArray()) {
            buffer2.putChar(ch);
        }
        assertEquals("Russian letters", testString2,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer2.array()));

        String testString3 = "Data";
        byte[] testBytes3 = new byte[]{68, 0, 97, 0, 116, 0, 97, 0, 0, 0, 0, 0};
        ByteBuffer buffer3 = ByteBuffer.wrap(testBytes3);
        buffer3.order(BYTE_ORDER);
        assertEquals("String which ends with \\0", testString3,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer3.array()));

        String testString4 = "";
        byte[] testBytes4 = new byte[]{0, 0};
        ByteBuffer buffer4 = ByteBuffer.wrap(testBytes4);
        buffer4.order(BYTE_ORDER);
        assertEquals("\\0 bytes", testString4,
                Converter.utf16BytesToString(BYTE_ORDER, buffer4.array()));

        assertEquals("Empty array", "", Converter.utf16BytesToString(BYTE_ORDER, new byte[0]));
        assertEquals("Null reference", "", Converter.utf16BytesToString(BYTE_ORDER, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void utf16BytesToStringExceptionTest() {
        Converter.utf16BytesToString(BYTE_ORDER, new byte[1]);
    }

    @Test
    public void stringToUtf16BytesTest() {
        byte[] expectedBytes = new byte[]{68, 0, 97, 0, 116, 0, 97, 0};
        byte[] actualBytes = Converter.stringToUtf16Bytes("Data", ByteOrder.LITTLE_ENDIAN);

        assertNotNull(actualBytes);
        assertArrayEquals(expectedBytes, actualBytes);

        byte[] expectedBytes1 = new byte[]{68, 0, 97, 0, 116, 0, 97, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        byte[] actualBytes1 = Converter.stringToUtf16Bytes("Data", ByteOrder.LITTLE_ENDIAN, true);

        assertNotNull(actualBytes1);
        assertEquals(expectedBytes1.length, actualBytes1.length);
        assertArrayEquals(expectedBytes1, actualBytes1);

        assertEquals(0, Converter.stringToUtf16Bytes(null, BYTE_ORDER).length);
        assertEquals(0, Converter.stringToUtf16Bytes("", BYTE_ORDER).length);

    }

    @Test(expected = IllegalArgumentException.class)
    public void stringToUtf16BytesExceptionTest() {
        Converter.stringToUtf16Bytes("12345678901234567890123456789012", BYTE_ORDER);
    }
}
