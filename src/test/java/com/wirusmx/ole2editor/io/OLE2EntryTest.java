package com.wirusmx.ole2editor.io;

import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteOrder;

public class OLE2EntryTest extends Assert {
    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest1(){
        new OLE2Entry(new byte[128], 5, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Name length = -1
     */
    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest2(){
        byte[] buffer = new byte[128];
        buffer[64] = -1;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Name length = 65
     */
    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest3(){
        byte[] buffer = new byte[128];
        buffer[64] = 65;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }


    /**
     * Type = -1
     */
    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest4(){
        byte[] buffer = new byte[128];
        buffer[66] = -1;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Type = 10
     */
    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest5(){
        byte[] buffer = new byte[128];
        buffer[66] = 10;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Color = -1
     */
    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest6(){
        byte[] buffer = new byte[128];
        buffer[67] = -1;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Color = 10
     */
    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest7(){
        byte[] buffer = new byte[128];
        buffer[67] = 10;
        new OLE2Entry(buffer, ByteOrder.BIG_ENDIAN);
    }

    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest8(){
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

    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest9(){
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

    @Test (expected = IllegalArgumentException.class)
    public void ole2EntryConstructorTest10(){
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

    @Test
    public void getNameAsStringTest(){
        OLE2Entry entry2 = new OLE2Entry(new byte[128], 0, ByteOrder.BIG_ENDIAN);
        assertEquals("Empty name", "", entry2.getNameAsString());
    }
}
