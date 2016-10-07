package com.wirusmx.ole2editor.application.controller;

import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.application.model.Model;
import com.wirusmx.ole2editor.application.view.FileChooser;
import com.wirusmx.ole2editor.application.view.View;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Controller {
    private Model model;
    private View view;

    /**
     * Construct a new controller with specified model and view
     *
     * @param model of application
     * @param view  current application view
     */
    public Controller(Model model, View view) {
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
        FileChooser fileChooser = view.getFileChooser();
        int dialogResult = fileChooser.showOpenDialog();
        if (dialogResult != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File currentFile = fileChooser.getSelectedFile();
        model.setCurrentFile(currentFile);


        try {
            view.updateStreams(model.getStreamsTree());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalFileStructure illegalFileStructure) {
            illegalFileStructure.printStackTrace();
        }

        view.setTitle("[" + currentFile.getName() + "]");
        view.update();


    }

    public File getCurrentFile() {
        return model.getCurrentFile();
    }

    public boolean isFileModified(){
        return model.isModified();
    }

    public boolean isFilePresent(){
        return model.getCurrentFile() != null;
    }
}
