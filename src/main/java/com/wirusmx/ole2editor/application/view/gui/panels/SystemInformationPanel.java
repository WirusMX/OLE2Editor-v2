package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.View;

import javax.swing.*;
import java.awt.*;

public class SystemInformationPanel extends JPanel {
    private DefaultListModel defaultListModel = new DefaultListModel();
    private JList list = new JList(defaultListModel);
    private JScrollPane scrollPane = new JScrollPane(list);

    private View view;

    public SystemInformationPanel(View view) {
        this.view = view;
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        defaultListModel.addElement("Header");
        defaultListModel.addElement("MSAT");
        defaultListModel.addElement("SAT");
        defaultListModel.addElement("SSAT");

        add(scrollPane, BorderLayout.CENTER);
    }
}
