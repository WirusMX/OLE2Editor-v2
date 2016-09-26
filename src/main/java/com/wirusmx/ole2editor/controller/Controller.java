package com.wirusmx.ole2editor.controller;

import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.model.Model;
import com.wirusmx.ole2editor.view.GUIView;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Controller {
    private Model model;
    private GUIView view;
    private File currentFile;

    public Controller(Model model, GUIView view) {
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

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int dialogResult = fileChooser.showOpenDialog(view);
        if (dialogResult != JFileChooser.APPROVE_OPTION) {
            return;
        }

        currentFile = fileChooser.getSelectedFile();
        view.setTitle("[" + currentFile.getName() + "]");
        view.update();
    }

    public String getStreamsTree() {

        String streamsTree = "";
        try {
            streamsTree = model.getStreamsTree();
        } catch (IllegalFileStructure illegalFileStructure) {
            // TODO
        } catch (IOException e) {
            // TODO
        }

        return streamsTree;
    }

    public File getCurrentFile() {
        return currentFile;
    }
}
