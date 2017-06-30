package com.wirusmx.ole2editor.utils;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ConverterTest extends Assert {

    @Test
    public void bytesToInt16ConverterTest() {
        short[] testNumbers = new short[]{-5555, -555, -2, -1, 0, 1, 2, 555, 5555};

        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            for (short i : testNumbers) {
                ByteBuffer buffer = ByteBuffer.allocate(2);
                buffer.order(bo);
                buffer.putShort(i);
                assertEquals("",
                        i,
                        Converter.bytesToInt16(bo, buffer.array()));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt16ConverterExceptionTest1() {
        Converter.bytesToInt16(ByteOrder.BIG_ENDIAN, new byte[1]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt16ConverterExceptionTest2() {
        Converter.bytesToInt16(ByteOrder.BIG_ENDIAN, new byte[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt16ConverterExceptionTest3() {
        Converter.bytesToInt16(ByteOrder.BIG_ENDIAN, null);
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

        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            for (int i : testNumbers) {
                ByteBuffer buffer = ByteBuffer.allocate(4);
                buffer.order(bo);
                buffer.putInt(i);
                assertEquals("",
                        i,
                        Converter.bytesToInt32(bo, buffer.array()));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt32ConverterExceptionTest1() {
        Converter.bytesToInt32(ByteOrder.LITTLE_ENDIAN, new byte[3]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt32ConverterExceptionTest2() {
        Converter.bytesToInt32(ByteOrder.LITTLE_ENDIAN, new byte[5]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt32ConverterExceptionTest3() {
        Converter.bytesToInt32(ByteOrder.LITTLE_ENDIAN, null);
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
        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            for (long i : testNumbers) {
                ByteBuffer buffer = ByteBuffer.allocate(8);
                buffer.order(bo);
                buffer.putLong(i);
                assertEquals("",
                        i,
                        Converter.bytesToInt64(bo, buffer.array()));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt64ConverterExceptionTest1() {
        Converter.bytesToInt64(ByteOrder.LITTLE_ENDIAN, new byte[7]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt64ConverterExceptionTest2() {
        Converter.bytesToInt64(ByteOrder.LITTLE_ENDIAN, new byte[9]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bytesToInt64ConverterExceptionTest3() {
        Converter.bytesToInt64(ByteOrder.LITTLE_ENDIAN, null);
    }

    @Test
    public void int64ToBytesTest() {
        byte[] expectedBytes = new BigInteger("" + Long.MAX_VALUE).toByteArray();
        byte[] actualBytes = Converter.int64ToBytes(Long.MAX_VALUE, ByteOrder.BIG_ENDIAN);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void utf16BytesToStringTest() {

        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            String testString = "qwertyuiopasdfghjklzxcvbnm";
            byte[] testBytes = new byte[testString.length() * 2];
            ByteBuffer buffer = ByteBuffer.wrap(testBytes);
            buffer.order(bo);
            for (char ch : testString.toCharArray()) {
                buffer.putChar(ch);
            }
            assertEquals("English letters", testString,
                    Converter.utf16BytesToString(bo, buffer.array()));

            String testString2 = "йцукенгшщзхъфывапролджэячсмитьбю";
            byte[] testBytes2 = new byte[testString2.length() * 2];
            ByteBuffer buffer2 = ByteBuffer.wrap(testBytes2);
            buffer2.order(bo);
            for (char ch : testString2.toCharArray()) {
                buffer2.putChar(ch);
            }
            assertEquals("Russian letters", testString2,
                    Converter.utf16BytesToString(bo, buffer2.array()));
        }

        String testString3 = "Data";
        byte[] testBytes3 = new byte[]{68, 0, 97, 0, 116, 0, 97, 0, 0, 0, 0, 0};
        ByteBuffer buffer3 = ByteBuffer.wrap(testBytes3);
        buffer3.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals("String which ends with \\0", testString3,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer3.array()));

        String testString4 = "";
        byte[] testBytes4 = new byte[]{0, 0};
        ByteBuffer buffer4 = ByteBuffer.wrap(testBytes4);
        buffer4.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals("\\0 bytes", testString4,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer4.array()));

        assertEquals("Empty array", "", Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, new byte[0]));
        assertEquals("Null reference", "", Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, null));

        String testString5 = "[05]Data";
        byte[] testBytes5 = new byte[]{5, 0, 68, 0, 97, 0, 116, 0, 97, 0, 0, 0, 0, 0};
        ByteBuffer buffer5 = ByteBuffer.wrap(testBytes5);
        buffer3.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals("String which ends with \\0", testString5,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer5.array()));

        String testString6 = "[19]Data";
        byte[] testBytes6 = new byte[]{25, 0, 68, 0, 97, 0, 116, 0, 97, 0, 0, 0, 0, 0};
        ByteBuffer buffer6 = ByteBuffer.wrap(testBytes6);
        buffer3.order(ByteOrder.LITTLE_ENDIAN);
        assertEquals("String which ends with \\0", testString6,
                Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, buffer6.array()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void utf16BytesToStringExceptionTest() {
        Converter.utf16BytesToString(ByteOrder.LITTLE_ENDIAN, new byte[1]);
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

        assertEquals(0, Converter.stringToUtf16Bytes(null, ByteOrder.LITTLE_ENDIAN).length);
        assertEquals(0, Converter.stringToUtf16Bytes("", ByteOrder.LITTLE_ENDIAN).length);

    }
}
