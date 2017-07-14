package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.DefaultExceptionsHandler;
import com.wirusmx.ole2editor.application.view.gui.GuiView;
import com.wirusmx.ole2editor.application.view.gui.dialogs.PropertyViewer;
import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.parsers.OLE2StreamParser;
import com.wirusmx.ole2editor.parsers.OLE2StreamParsersFactory;
import com.wirusmx.ole2editor.parsers.Property;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.List;

public class PropertiesPanel extends MyPanel {
    private static final boolean SKIP_UNKNOWNS = false;

    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;
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

        tableModel.setColumnIdentifiers(
                new String[]{
                        uiResourceBundle.getString("properties_panel_header_offset"),
                        uiResourceBundle.getString("properties_panel_header_size"),
                        uiResourceBundle.getString("properties_panel_header_description"),
                        uiResourceBundle.getString("properties_panel_header_value")});
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
        } catch (IllegalFileStructure | IOException | InstantiationException | IllegalAccessException e) {
            DefaultExceptionsHandler.handle(view, e);
        }
    }

    private void showSectorData(int currentSector) {
        clearTable();
        switch (currentSector) {
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
            default: {
                label.setText("Sector #" + currentSector);
            }
        }

    }

    private void showStreamData(LinkedOLE2Entry currentStream) throws IOException, IllegalFileStructure, InstantiationException, IllegalAccessException {
        clearTable();
        label.setText(currentStream.getAbsolutePath() + ":");

        byte[] streamBytes = view.getController().getStreamBytes(currentStream);
        OLE2StreamParser parser = OLE2StreamParsersFactory.getInstance().getOle2StreamParser(streamBytes);
        java.util.List<Property> streamPropertiesList = parser.parse(currentStream, streamBytes);
        MouseListener[] mouseListeners = table.getMouseListeners();
        if (mouseListeners.length > 0){
            for (MouseListener ml : mouseListeners){
                if (ml.getClass().equals(MyMouseListener.class)) {
                    table.removeMouseListener(ml);
                }
            }
        }

        table.addMouseListener(new MyMouseListener(streamPropertiesList));
        for (Property p : streamPropertiesList) {

            if (p.isUnknown() && SKIP_UNKNOWNS) {
                continue;
            }

            Object[] row = new Object[]{
                    String.format("0x%08X", p.getOffset()),
                    "" + p.getLength(),
                    p.getDescription(),
                    ""
            };

            switch (p.getType()){
                case TEXT: {
                    String val = p.getValue().toString();
                    if (val.length() > 25){
                        row[3] = val.substring(0, 25) + " <...>";
                    } else {
                        row[3] = val;
                    }
                    break;
                }

                case IMAGE:
                case BYTES: {
                    row[3] = "<...>";
                }
            }

            tableModel.addRow(row);
        }
    }

    @Override
    public void reset() {

    }

    private void clearTable() {
        int rowsCount = tableModel.getRowCount() - 1;
        for (int i = rowsCount; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    private class MyMouseListener implements MouseListener {
        private java.util.List<Property> streamPropertiesList;

        public MyMouseListener(List<Property> streamPropertiesList) {
            this.streamPropertiesList = streamPropertiesList;

        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int pos = table.getSelectedRow();
                Property property = streamPropertiesList.get(pos);
                new PropertyViewer(property);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
