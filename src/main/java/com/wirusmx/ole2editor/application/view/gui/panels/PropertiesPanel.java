package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.DefaultExceptionsHandler;
import com.wirusmx.ole2editor.application.view.gui.GuiView;
import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.IOException;

public class PropertiesPanel extends MyPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane ;
    private JLabel label;

    public PropertiesPanel(GuiView view) {
        super(view);
    }



    @Override
    public void init() {
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

        tableModel.setColumnIdentifiers(new String[]{"Offset", "Hex", "ASCII", "Description"});
        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        try {
            LinkedOLE2Entry currentStream = view.getController().getCurrentStream();
            if (currentStream != null) {
                showStreamData(currentStream);
            }

            int currentSector = view.getController().getCurrentSector();
            if (currentSector > -5) {
                showSectorData(currentSector);
            }
        } catch (IllegalFileStructure | IOException e) {
            DefaultExceptionsHandler.handle(view, e);
        }
    }

    private void showSectorData(int currentSector) {
        clearTable();
        switch (currentSector){
            case -1: {
                label.setText("HEADER:");
                break;
            }

            case -2: {
                label.setText("MSAT:");
                break;
            }

            case -3: {
                label.setText("SAT:");
                break;
            }

            case -4: {
                label.setText("SSAT:");
                break;
            }
            default:{
                label.setText("Sector #" + currentSector);
            }
        }

    }

    private void showStreamData(LinkedOLE2Entry currentStream) throws IOException, IllegalFileStructure {
        clearTable();
        label.setText(currentStream.getAbsolutePath() + ":");
        byte[] bytes = currentStream.getRawData();
        for (int i = 0; i < bytes.length; i++){
            tableModel.addRow(new String[]{String.format("%05d", i),
                    String.format("%02X", bytes[i] & 0xFF),
                    "" + (char) bytes[i],
                    ""});
        }
    }

    @Override
    public void reset() {

    }

    private void clearTable(){
        int rowsCount = tableModel.getRowCount() - 1;
        for (int i = rowsCount; i >= 0; i--){
            tableModel.removeRow(i);
        }
    }
}
