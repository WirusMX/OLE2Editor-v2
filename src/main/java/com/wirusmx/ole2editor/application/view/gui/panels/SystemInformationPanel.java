package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import javax.swing.*;
import java.awt.*;

public class SystemInformationPanel extends MyPanel {
    private DefaultListModel defaultListModel;
    private JList list;
    private JScrollPane scrollPane;

    public SystemInformationPanel(GuiView view) {
        super(view);
    }

    @Override
    public void init() {
        defaultListModel = new DefaultListModel();
        list = new JList(defaultListModel);
        scrollPane = new JScrollPane(list);

        setLayout(new BorderLayout());
        defaultListModel.addElement("Header");
        defaultListModel.addElement("MSAT");
        defaultListModel.addElement("SAT");
        defaultListModel.addElement("SSAT");

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void update() {

    }
}
