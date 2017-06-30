package com.wirusmx.ole2editor.application.model;

import com.wirusmx.ole2editor.application.controller.Controller;
import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.io.OLE2Entry;
import com.wirusmx.ole2editor.io.OLE2InputStream;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Model {

    private Controller controller;
    private boolean modified = false;
    private File currentFile;
    private LinkedOLE2Entry currentStream;
    private LinkedOLE2Entry selectedStream;
    private int currentSector;


    public boolean isModified() {
        return modified && currentFile != null;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(File currentFile) {
        this.currentFile = currentFile;
        modified = false;
        currentStream = null;
        currentSector = -5;
    }

    public LinkedOLE2Entry getSelectedStream() {
        return selectedStream;
    }

    public void setSelectedStream(LinkedOLE2Entry selectedStream) {
        this.selectedStream = selectedStream;
    }

    public LinkedOLE2Entry getCurrentStream() {
        return currentStream;
    }

    public void setCurrentStream(LinkedOLE2Entry currentStream) {
        this.currentStream = currentStream;
        currentSector = -5;
    }

    public int getCurrentSector() {
        return currentSector;
    }

    public void setCurrentSector(int currentSector) {
        this.currentSector = currentSector;
        currentStream = null;
    }

    public LinkedOLE2Entry getStreamsTree() throws IOException, IllegalFileStructure {
        List<OLE2Entry> entries = getOle2EntriesList();

        return LinkedOLE2Entry.buildByEntriesList(entries);
    }

    public List<LinkedOLE2Entry> getWidowedStreamsList() throws IOException, IllegalFileStructure {
        List<OLE2Entry> entries = getOle2EntriesList();
        LinkedOLE2Entry linkedEntry = LinkedOLE2Entry.buildByEntriesList(entries);
        List<LinkedOLE2Entry> unlinkedEntries = new ArrayList<>();

        while (linkedEntry != null) {
            if (linkedEntry.isUnlinked()) {
                unlinkedEntries.add(linkedEntry);
            }

            linkedEntry = linkedEntry.getNext();
        }

        return unlinkedEntries;
    }

    private List<OLE2Entry> getOle2EntriesList() throws IOException, IllegalFileStructure {
        List<OLE2Entry> entries = new ArrayList<>();

        try (OLE2InputStream is = new OLE2InputStream(currentFile.getAbsolutePath())) {
            while (is.hasNextEntry()) {
                entries.add(is.readNextEntry());
            }
        }
        return entries;
    }

    /**
     * Read <code>(to - from)</code> bytes or less, if stream length < <code>to</code>,
     * from OLE2 stream
     *
     * @param ole2Entry - OLE2 entry for reading stream bytes
     * @param from      - first byte for reading position (included)
     * @param to        - last byte for reading position (not included)
     * @return array of bytes. Array length is:
     * <code>(to - from)</code> if parameter <code>to</code> less or equals stream length
     * <code>(ole2Entry.getSize() - from)</code> if parameter <code>to</code> more then stream length
     * 0 otherwise
     * @throws IOException              if some i/o problems occur
     * @throws IllegalFileStructure     if file has illegal structure
     * @throws IllegalArgumentException if method parameter unacceptable
     */
    public byte[] getStreamBytes(LinkedOLE2Entry ole2Entry, int from, int to) throws IOException, IllegalFileStructure {
        if (from > to) {
            throw new IllegalArgumentException("Parameter \"to\" must be greater then \"from\"");
        }

        if (from < 0) {
            throw new IllegalArgumentException("Parameter \"from\" must be positive");
        }

        byte[] buffer = new byte[to - from];
        try (OLE2InputStream ole2InputStream = new OLE2InputStream(currentFile.getAbsolutePath())) {
            boolean isEntryFound = false;
            while (ole2InputStream.hasNextEntry()) {
                if (ole2Entry.toOLE2Entry().equals(ole2InputStream.readNextEntry())) {
                    isEntryFound = true;
                    break;
                }
            }

            if (isEntryFound) {
                int i = 0;
                while (i < from) {
                    ole2InputStream.read();
                    i++;
                }

                int bytesCount = ole2InputStream.read(buffer);
                if (bytesCount < buffer.length) {
                    byte[] tempBuffer = new byte[bytesCount];
                    System.arraycopy(buffer, 0, tempBuffer, 0, bytesCount);
                    return tempBuffer;
                }

                return buffer;
            }

        }

        return new byte[0];
    }


}
