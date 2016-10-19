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
    }

    public LinkedOLE2Entry getStreamsTree() throws IOException, IllegalFileStructure {
        List<OLE2Entry> entries = getOle2EntriesList();

        return LinkedOLE2Entry.buildByEntriesList(entries);
    }

    public LinkedOLE2Entry getWidowedStreamsList() throws IOException, IllegalFileStructure {
        List<OLE2Entry> entries = getOle2EntriesList();
        LinkedOLE2Entry linkedEntries = LinkedOLE2Entry.buildByEntriesList(entries);

        return null;
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
}
