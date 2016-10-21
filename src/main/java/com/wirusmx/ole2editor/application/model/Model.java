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

        while (linkedEntry != null){
            if (linkedEntry.isUnlincked()){
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

    public byte[] getStreamBytes(LinkedOLE2Entry currentStream, int from, int to) throws IOException, IllegalFileStructure {
        byte[] buffer = new byte[to - from];
        try (OLE2InputStream is = new OLE2InputStream(currentFile.getAbsolutePath())){
            boolean isEntryFound = false;
            while (is.hasNextEntry()){
                if (currentStream.toOLE2Entry().equals(is.readNextEntry())){
                    isEntryFound = true;
                    break;
                }
            }

            if (isEntryFound){
                int i = 0;
                while (i < from){
                    is.read();
                    i++;
                }

                is.read(buffer);
            }

        }

        return buffer;
    }
}
