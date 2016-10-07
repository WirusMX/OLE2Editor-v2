package com.wirusmx.ole2editor.application.view;

import com.wirusmx.ole2editor.application.controller.Controller;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import java.io.File;


public interface View {
    void init();

    void setController(Controller controller);

    void setTitle(String title);

    void update();

    FileChooser getFileChooser();

    Controller getController();

    void updateStreamsList(LinkedOLE2Entry tree);

    void updateFilesList(File file);

    void updateStatus1(String value);

    void updateStatus2(String value);
}