package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.View;

import javax.swing.*;
import java.awt.*;

public class PropertiesPanel extends JPanel {
    private View view;

    public PropertiesPanel(View view){
        this.view = view;
        init();
    }

    private void init(){
        setLayout(new BorderLayout());
        add(new JLabel("PropertiesPanel"), BorderLayout.CENTER);
    }
}
