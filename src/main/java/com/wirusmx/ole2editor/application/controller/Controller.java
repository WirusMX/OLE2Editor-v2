package com.wirusmx.ole2editor.application.controller;

import com.wirusmx.ole2editor.application.model.Model;
import com.wirusmx.ole2editor.application.view.gui.GuiView;
import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

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

    public LinkedOLE2Entry getCurrentStream() {
        return model.getCurrentStream();
    }

    public void setCurrentStream(LinkedOLE2Entry currentStream) {
        model.setCurrentStream(currentStream);
        view.update();
    }

    public int getCurrentSector() {
        return model.getCurrentSector();
    }

    public void setCurrentSector(int currentSector) {
        model.setCurrentSector(currentSector);
        view.update();
    }


    public List<LinkedOLE2Entry> getWidowedStreamsList() throws IOException, IllegalFileStructure {
        return model.getWidowedStreamsList();
    }

    public byte[] getStreamBytes(LinkedOLE2Entry currentStream, int from, int to) throws IOException, IllegalFileStructure {
        return model.getStreamBytes(currentStream, from, to);
    }
}
