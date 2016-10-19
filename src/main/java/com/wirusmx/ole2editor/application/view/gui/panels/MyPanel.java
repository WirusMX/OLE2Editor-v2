package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import javax.swing.*;

public abstract class MyPanel extends JPanel {
    GuiView view;

    public MyPanel(GuiView view){
        this.view = view;
        init();
    }

    public abstract void init();

    public abstract void update();

    public abstract void reset();
}
