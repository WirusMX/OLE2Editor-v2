package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.View;

import javax.swing.*;
import java.awt.*;

public class SystemInformationPanel extends JPanel {
    private View view;

    public SystemInformationPanel(View view) {
        this.view = view;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        add(new JLabel("SystemInformationPanel"), BorderLayout.CENTER);
    }
}
