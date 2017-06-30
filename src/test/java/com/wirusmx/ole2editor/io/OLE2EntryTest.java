package com.wirusmx.ole2editor.io;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class OLE2EntryTest extends Assert {
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest1() {
        new OLE2Entry(new byte[128], 5, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Name length = -1
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest2() {
        byte[] buffer = new byte[128];
        buffer[64] = -1;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Name length = 65
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest3() {
        byte[] buffer = new byte[128];
        buffer[64] = 65;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }


    /**
     * Type = -1
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest4() {
        byte[] buffer = new byte[128];
        buffer[66] = -1;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Type = 10
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest5() {
        byte[] buffer = new byte[128];
        buffer[66] = 10;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Color = -1
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest6() {
        byte[] buffer = new byte[128];
        buffer[67] = -1;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Color = 10
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest7() {
        byte[] buffer = new byte[128];
        buffer[67] = 10;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Entry name array is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest8() {
        new OLE2Entry(null,
                -1,
                OLE2Entry.EntryType.EMPTY,
                OLE2Entry.EntryColor.BLACK,
                -1,
                -1,
                -1,
                null,
                null,
                null,
                null,
                -1,
                -1,
                null,
                ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Entry name array has length more then 64
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest9() {
        new OLE2Entry(new byte[66],
                -1,
                OLE2Entry.EntryType.EMPTY,
                OLE2Entry.EntryColor.BLACK,
                -1,
                -1,
                -1,
                null,
                null,
                null,
                null,
                -1,
                -1,
                null,
                ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Entry name argument has value more then 64
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest10() {
        new OLE2Entry(new byte[64],
                65,
                OLE2Entry.EntryType.EMPTY,
                OLE2Entry.EntryColor.BLACK,
                -1,
                -1,
                -1,
                null,
                null,
                null,
                null,
                -1,
                -1,
                null,
                ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Entry name argument has value less then 0
     */
    @Test(expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest11() {
        new OLE2Entry(new byte[64],
                -1,
                OLE2Entry.EntryType.EMPTY,
                OLE2Entry.EntryColor.BLACK,
                -1,
                -1,
                -1,
                null,
                null,
                null,
                null,
                -1,
                -1,
                null,
                ByteOrder.LITTLE_ENDIAN);
    }

    @Test
    public void getNameAsStringTest() {
        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer b = ByteBuffer.allocate(64);
            b.order(bo);
            b.putChar('D');
            b.putChar('a');
            b.putChar('t');
            b.putChar('A');

            OLE2Entry entry1 = new OLE2Entry(b.array(),
                    10,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);

            assertEquals("DatA", entry1.getNameAsString());
        }

        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer b = ByteBuffer.allocate(128);
            b.order(bo);
            b.putChar('D');
            b.putChar('a');
            b.putChar('t');
            b.putChar('A');

            b.putShort(64, (short) 10);

            OLE2Entry entry1 = new OLE2Entry(b.array(), bo);

            assertEquals(bo.toString(), "DatA", entry1.getNameAsString());
        }

        OLE2Entry entry2 = new OLE2Entry(new byte[128], 0, ByteOrder.BIG_ENDIAN);
        assertEquals("Empty name", "", entry2.getNameAsString());
    }

    @Test
    public void getRawDataTest(){

    }

    @Test
    public void toStringTest(){
        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer b = ByteBuffer.allocate(64);
            b.order(bo);
            b.putChar('D');
            b.putChar('a');
            b.putChar('t');
            b.putChar('A');

            OLE2Entry entry1 = new OLE2Entry(b.array(),
                    10,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);

            assertEquals("DatA", entry1.toString());
        }
    }

    /**
     * Where o == this
     */
    @Test
    public void equalsTest1 (){
        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer b = ByteBuffer.allocate(64);
            b.order(bo);
            b.putChar('D');
            b.putChar('a');
            b.putChar('t');
            b.putChar('A');

            OLE2Entry entry1 = new OLE2Entry(b.array(),
                    10,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);


            OLE2Entry entry2 = entry1;
            assertTrue(entry2.equals(entry1));
        }
    }

    /**
     * Where o != this
     */
    @Test
    public void equalsTest2 (){
        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer b = ByteBuffer.allocate(64);
            b.order(bo);
            b.putChar('D');
            b.putChar('a');
            b.putChar('t');
            b.putChar('A');

            OLE2Entry entry1 = new OLE2Entry(b.array(),
                    10,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);


            OLE2Entry entry2 = new OLE2Entry(b.array(),
                    10,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);
            assertTrue(entry2.equals(entry1));
        }
    }

    /**
     * Where o == null
     */
    @Test
    public void equalsTest3 (){
        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer b = ByteBuffer.allocate(64);
            b.order(bo);
            b.putChar('D');
            b.putChar('a');
            b.putChar('t');
            b.putChar('A');

            OLE2Entry entry1 = new OLE2Entry(b.array(),
                    10,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);


            assertFalse(entry1.equals(null));
        }
    }

    /**
     * Where o not a OLE2Entry
     */
    @Test
    public void equalsTest4 (){
        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer b = ByteBuffer.allocate(64);
            b.order(bo);
            b.putChar('D');
            b.putChar('a');
            b.putChar('t');
            b.putChar('A');

            OLE2Entry entry1 = new OLE2Entry(b.array(),
                    10,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);


            assertFalse(entry1.equals(""));
        }
    }
    /**
     * Where names length is different
     */
    @Test
    public void equalsTest5 (){
        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer b = ByteBuffer.allocate(64);
            b.order(bo);
            b.putChar('D');
            b.putChar('a');
            b.putChar('t');
            b.putChar('A');

            OLE2Entry entry1 = new OLE2Entry(b.array(),
                    10,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);


            OLE2Entry entry2 = new OLE2Entry(b.array(),
                    12,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);
            assertFalse(entry2.equals(entry1));
        }
    }

    /**
     * Where names is different, but names length are equals
     */
    @Test
    public void equalsTest6 (){
        for (ByteOrder bo : new ByteOrder[]{ByteOrder.BIG_ENDIAN, ByteOrder.LITTLE_ENDIAN}) {
            ByteBuffer b = ByteBuffer.allocate(64);
            b.order(bo);
            b.putChar('D');
            b.putChar('a');
            b.putChar('t');
            b.putChar('A');

            OLE2Entry entry1 = new OLE2Entry(b.array(),
                    10,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);


            b.clear();
            b = ByteBuffer.allocate(64);
            b.order(bo);
            b.putChar('T');
            b.putChar('e');
            b.putChar('s');
            b.putChar('T');

            OLE2Entry entry2 = new OLE2Entry(b.array(),
                    12,
                    OLE2Entry.EntryType.EMPTY,
                    OLE2Entry.EntryColor.BLACK,
                    -1,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    bo);
            assertFalse(entry2.equals(entry1));
        }
    }
}
