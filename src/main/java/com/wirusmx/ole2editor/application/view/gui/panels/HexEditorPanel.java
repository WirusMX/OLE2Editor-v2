package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import javax.swing.*;
import java.awt.*;

public class HexEditorPanel extends MyPanel {
    public HexEditorPanel(GuiView view) {
        super(view);
    }

    @Override
    public void init(){
        setLayout(new BorderLayout());
        add(new JLabel("HexEditorPanel"), BorderLayout.CENTER);
    }

    @Override
    public void update(){
    }

    @Override
    public void reset() {

    }
}
