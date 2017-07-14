package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import javax.swing.*;
import java.util.ResourceBundle;

public abstract class MyPanel extends JPanel {
    GuiView view;

    ResourceBundle uiResourceBundle = ResourceBundle.getBundle("lang.ui");

    public MyPanel(GuiView view){
        this.view = view;
        init();
    }

    public abstract void init();

    public abstract void update();

    public abstract void reset();
}
