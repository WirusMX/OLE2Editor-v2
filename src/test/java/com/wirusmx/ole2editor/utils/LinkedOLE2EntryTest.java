package com.wirusmx.ole2editor.utils;

import com.wirusmx.ole2editor.io.OLE2Entry;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class LinkedOLE2EntryTest extends Assert {
    private List<OLE2Entry> initEntriesList() {
        List<OLE2Entry> entries = new ArrayList<>();
        entries.add(new OLE2EntryStub("RootEntry", OLE2Entry.EntryType.ROOT_STORAGE, -1, -1, 3)); //0
        entries.add(new OLE2EntryStub("Data", OLE2Entry.EntryType.USER_STORAGE, -1, -1, -1)); //1
        entries.add(new OLE2EntryStub("1Table", OLE2Entry.EntryType.USER_STREAM, 1, -1, -1)); //2
        entries.add(new OLE2EntryStub("WordDocument", OLE2Entry.EntryType.USER_STREAM, 9, 5, -1)); //3
        entries.add(new OLE2EntryStub("SummaryInformation", OLE2Entry.EntryType.USER_STREAM, -1, -1, -1)); //4
        entries.add(new OLE2EntryStub("DocumentSummaryInformation", OLE2Entry.EntryType.USER_STREAM, 4, -1, -1)); //5
        entries.add(new OLE2EntryStub("MsoDataStore", OLE2Entry.EntryType.USER_STORAGE, -1, -1, 7)); //6
        entries.add(new OLE2EntryStub("Item", OLE2Entry.EntryType.USER_STREAM, -1, 8, -1)); //7
        entries.add(new OLE2EntryStub("Properties", OLE2Entry.EntryType.USER_STREAM, -1, -1, -1)); //8
        entries.add(new OLE2EntryStub("CompObj", OLE2Entry.EntryType.USER_STREAM, 2, 6, -1)); //9

        return entries;
    }

    @Test
    public void buildByEntriesListTest() {
        List<OLE2Entry> entries = initEntriesList();

        LinkedOLE2Entry root = LinkedOLE2Entry.buildByEntriesList(entries);
        assertEquals(0, root.getFid());
        assertNull(root.getLeft());
        assertNull(root.getRight());
        assertNotNull(root.getChild());
        assertNull(root.getParent());


        LinkedOLE2Entry entry3 = root.getChild();
        assertEquals(3, entry3.getFid());
        assertNull(entry3.getChild());
        assertNotNull(entry3.getLeft());
        assertNotNull(entry3.getRight());
        assertNotNull(entry3.getParent());
        assertEquals(0, entry3.getParent().getFid());


        LinkedOLE2Entry entry9 = entry3.getLeft();
        assertEquals(9, entry9.getFid());
        assertNull(entry9.getChild());
        assertNotNull(entry9.getLeft());
        assertNotNull(entry9.getRight());
        assertNotNull(entry9.getParent());
        assertEquals(0, entry9.getParent().getFid());

        LinkedOLE2Entry entry5 = entry3.getRight();
        assertEquals(5, entry5.getFid());
        assertNull(entry5.getChild());
        assertNotNull(entry5.getLeft());
        assertNull(entry5.getRight());
        assertNotNull(entry5.getParent());
        assertEquals(0, entry5.getParent().getFid());

        LinkedOLE2Entry entry2 = entry9.getLeft();
        assertEquals(2, entry2.getFid());
        assertNull(entry2.getChild());
        assertNotNull(entry2.getLeft());
        assertNull(entry2.getRight());
        assertNotNull(entry2.getParent());
        assertEquals(0, entry2.getParent().getFid());

        LinkedOLE2Entry entry6 = entry9.getRight();
        assertEquals(6, entry6.getFid());
        assertNotNull(entry6.getChild());
        assertNull(entry6.getLeft());
        assertNull(entry6.getRight());
        assertNotNull(entry6.getParent());
        assertEquals(0, entry6.getParent().getFid());

        LinkedOLE2Entry entry4 = entry5.getLeft();
        assertEquals(4, entry4.getFid());
        assertNull(entry4.getChild());
        assertNull(entry4.getLeft());
        assertNull(entry4.getRight());
        assertNotNull(entry4.getParent());
        assertEquals(0, entry4.getParent().getFid());

        LinkedOLE2Entry entry1 = entry2.getLeft();
        assertEquals(1, entry1.getFid());
        assertNull(entry1.getChild());
        assertNull(entry1.getLeft());
        assertNull(entry1.getRight());
        assertNotNull(entry1.getParent());
        assertEquals(0, entry1.getParent().getFid());

        LinkedOLE2Entry entry7 = entry6.getChild();
        assertEquals(7, entry7.getFid());
        assertNull(entry7.getChild());
        assertNull(entry7.getLeft());
        assertNotNull(entry7.getRight());
        assertNotNull(entry7.getParent());
        assertEquals(6, entry7.getParent().getFid());

        LinkedOLE2Entry entry8 = entry7.getRight();
        assertEquals(8, entry8.getFid());
        assertNull(entry8.getChild());
        assertNull(entry8.getLeft());
        assertNull(entry8.getRight());
        assertNotNull(entry8.getParent());
        assertEquals(6, entry8.getParent().getFid());
    }

    @Test
    public void entriesListTest(){
        List<OLE2Entry> entries = initEntriesList();
        LinkedOLE2Entry root = LinkedOLE2Entry.buildByEntriesList(entries);
        List<LinkedOLE2Entry> rootStorageEntries = root.entriesList();
        String[] values = new String[]{"Data",
                "MsoDataStore",
                "1Table",
                "CompObj",
                "DocumentSummaryInformation",
                "SummaryInformation",
                "WordDocument"
        };

        assertNotNull(rootStorageEntries);
        assertEquals(values.length, rootStorageEntries.size());
        for (int i = 0; i < values.length; i++){
            assertEquals(values[i], rootStorageEntries.get(i).getNameAsString());
        }
    }

    private static class OLE2EntryStub extends OLE2Entry {
        OLE2EntryStub(String name, EntryType type, int left, int right, int child) {
            super(Converter.stringToUtf16Bytes(name, ByteOrder.LITTLE_ENDIAN, true),
                    name.length() * 2 + 2,
                    type,
                    EntryColor.BLACK,
                    left,
                    right,
                    child,
                    null,
                    null,
                    null,
                    null,
                    -1,
                    -1,
                    null,
                    ByteOrder.LITTLE_ENDIAN);

        }

    }
}
