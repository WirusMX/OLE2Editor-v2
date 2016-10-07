package com.wirusmx.ole2editor.application.view.gui;

import com.wirusmx.ole2editor.application.view.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class GuiFileChooser implements FileChooser {
    private Component parent;

    private JFileChooser fileChooser;

    public GuiFileChooser(Component parent) {
        this.parent = parent;
    }

    @Override
    public int showOpenDialog() {
        fileChooser = new JFileChooser();
        return fileChooser.showOpenDialog(parent);
    }

    @Override
    public File getSelectedFile() {
        if (fileChooser == null) {
            return null;
        }

        return fileChooser.getSelectedFile();
    }
}
