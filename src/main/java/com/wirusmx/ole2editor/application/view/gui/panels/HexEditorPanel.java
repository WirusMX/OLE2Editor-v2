package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.GuiView;
import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.io.IOException;

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

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPane = new JScrollPane(table);

        Panel panel = new Panel(new BorderLayout());
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        String[] columnHeaders = new String[33];
        columnHeaders[0] = " Offset ";
        for (int i = 0; i < 16; i++){
            columnHeaders[i + 1] = " " + Integer.toHexString(i).toUpperCase() + " ";
            columnHeaders[i + 17] = " " + Integer.toHexString(i).toUpperCase() + " ";
        }
        tableModel.setColumnIdentifiers(columnHeaders);
        add(panel, BorderLayout.CENTER);
        updateColumnsWidth();
    }

    @Override
    public void update(){
        for (int i = tableModel.getRowCount() - 1; i >= 0; i--){
            tableModel.removeRow(i);
        }

        LinkedOLE2Entry currentStream = view.getController().getCurrentStream();
        LinkedOLE2Entry selectedStream = view.getController().getSelectedStream();

        if (selectedStream != null && !selectedStream.equals(currentStream)){
            currentStream = selectedStream;
        }

        if (currentStream == null){
            return;
        }

        byte[] currentStreamRawData;
        try {
            currentStreamRawData = view.getController().getStreamBytes(currentStream);
        } catch (IOException | IllegalFileStructure e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
            return;
        }

        int pos = 1;
        int lineNumber = 0;
        String stringValue = "";
        String[] row = new String[33];
        row[0] = String.format("%08X", lineNumber);
        for (int i = 0; i < currentStreamRawData.length; i++){
            if (pos == 17){
                tableModel.addRow(row);

                pos = 1;
                lineNumber += 16;
                stringValue = "";
                row = new String[33];
                row[0] = String.format("%08X", lineNumber);

            }

            row[pos] = String.format("%02X", currentStreamRawData[i] & 0xFF).toUpperCase();
            row[pos + 16] = new String(new byte[]{currentStreamRawData[i]});
            stringValue += (char)currentStreamRawData[i];

            pos++;
        }

        if (pos != 17){
            tableModel.addRow(row);
        }

        updateColumnsWidth();
    }


    @Override
    public void reset() {

    }

    private void updateColumnsWidth(){
        JTableHeader tableHeader = table.getTableHeader();
        for (int i = 0; i < table.getColumnCount(); i++){
            TableColumn column = table.getColumnModel().getColumn(i);
            int prefferedWidth =
                    Math.round(
                            (float) tableHeader.getFontMetrics(tableHeader.getFont()).
                                    getStringBounds(tableHeader.getTable().getColumnName(i),
                                            tableHeader.getGraphics()).getWidth());

            column.setPreferredWidth(prefferedWidth + 10);
        }
    }
}
