package com.wirusmx.ole2editor.application.view.gui.wrappers;

import java.io.File;

public class FileListElement implements JListElementWrapper<File> {
    private File file;

    public FileListElement(File file) {
        this.file = file;
    }

    public File getObject() {
        return file;
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
