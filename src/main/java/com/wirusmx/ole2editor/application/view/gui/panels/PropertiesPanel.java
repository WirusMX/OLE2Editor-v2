package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PropertiesPanel extends MyPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane ;

    public PropertiesPanel(GuiView view) {
        super(view);
    }

    @Override
    public void init() {
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        scrollPane = new JScrollPane(table);

        tableModel.setColumnIdentifiers(new String[]{"#", "Bytes", "Values", "Description"});
        tableModel.addRow(new String[]{"1", "2", "3", "4"});
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void update() {

    }
}
