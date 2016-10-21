package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;

public class HexEditorPanel extends MyPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane ;
    private JLabel label;

    public HexEditorPanel(GuiView view) {
        super(view);
    }

    @Override
    public void init(){
        setLayout(new BorderLayout());

        label = new JLabel("");

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        scrollPane = new JScrollPane(table);

        Panel panel = new Panel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        String[] columnHeaders = new String[33];
        Arrays.fill(columnHeaders, "");
        for (int i = 0; i < 16; i++){
            columnHeaders[i + 1] = Integer.toHexString(i).toUpperCase();
        }
        tableModel.setColumnIdentifiers(columnHeaders);
        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void update(){
    }

    @Override
    public void reset() {

    }
}
