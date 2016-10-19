package com.wirusmx.ole2editor.application.controller;

import com.wirusmx.ole2editor.application.model.Model;
import com.wirusmx.ole2editor.application.view.gui.GuiView;
import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Controller {
    private Model model;
    private GuiView view;

    /**
     * Construct a new controller with specified model and view
     *
     * @param model of application
     * @param view  current application view
     */
    public Controller(Model model, GuiView view) {
        this.model = model;
        this.view = view;
    }

    public void init() {
        model.setController(this);
        view.setController(this);
        view.init();
    }

    public void exit() {
        System.exit(0);
    }

    public void openFile(JFrame frame) {
        JFileChooser fileChooser = new JFileChooser();
        int dialogResult = fileChooser.showOpenDialog(frame);
        if (dialogResult != JFileChooser.APPROVE_OPTION) {
            return;
        }

        openFile(fileChooser.getSelectedFile());
    }

    public void openFile(File file) {
        model.setCurrentFile(file);

        view.setTitle("[" + file.getName() + "]");

        view.reset();
    }

    public File getCurrentFile() {
        return model.getCurrentFile();
    }

    public boolean isFileModified() {
        return model.isModified();
    }

    public boolean isFilePresent() {
        return model.getCurrentFile() != null;
    }

    public LinkedOLE2Entry getStreamsTree() throws IOException, IllegalFileStructure {
        return model.getStreamsTree();
    }

    public LinkedOLE2Entry getWidowedStreamsTree() throws IOException, IllegalFileStructure {
        return model.getWidowedStreamsList();
    }

}
