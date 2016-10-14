package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PropertiesPanel extends JPanel {
    private DefaultTableModel tableModel = new DefaultTableModel();
    private JTable table = new JTable(tableModel) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JScrollPane scrollPane = new JScrollPane(table);

    private View view;

    public PropertiesPanel(View view){
        this.view = view;
        init();
    }

    private void init(){
        setLayout(new BorderLayout());
        tableModel.setColumnIdentifiers(new String[]{"#", "Bytes", "Values", "Description"});
        tableModel.addRow(new String[]{"1", "2", "3", "4"});
        add(scrollPane, BorderLayout.CENTER);
    }
}
