package com.wirusmx.ole2editor.application.view;

import com.wirusmx.ole2editor.application.controller.Controller;


public interface View {
    void init();

    void setController(Controller controller);

    void setTitle(String title);


    FileChooser getFileChooser();

    Controller getController();

    void update();

    int getVisiblePanels();
}