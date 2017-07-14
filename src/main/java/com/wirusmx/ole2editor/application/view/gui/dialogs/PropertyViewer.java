package com.wirusmx.ole2editor.application.view.gui.dialogs;

import com.wirusmx.ole2editor.parsers.Property;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import static java.awt.BorderLayout.NORTH;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import static javax.swing.SwingConstants.CENTER;

public class PropertyViewer extends JDialog {
    private Property property;

    private JButton valueButton;
    private JButton bytesButton;
    private JPanel valuesPanel;


    public PropertyViewer(Property property) {
        this.property = property;
        init();
    }

    private JDialog thisDialog = this;

    private void init() {
        setLayout(new BorderLayout(5, 5));
        setSize(500, 400);
        setModal(true);

        JPanel topLabelsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        topLabelsPanel.add(new JLabel("Offset: " + String.format("0x%08X", property.getOffset()) +
                " (" + property.getOffset() + ")"));

        topLabelsPanel.add(new JLabel("Size: " + property.getLength() + " bytes"));
        add(topLabelsPanel, NORTH);

        JSplitPane splitPane = new JSplitPane(VERTICAL_SPLIT);
        JTextArea descriptionTextArea = new JTextArea(property.getDescription());
        descriptionTextArea.setEditable(false);
        splitPane.setTopComponent(descriptionTextArea);

        valuesPanel = new JPanel(new BorderLayout(5, 5));
        JPanel viewButtonsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        valueButton = new JButton("Value");
        valueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showValue();
            }
        });
        viewButtonsPanel.add(valueButton);

        bytesButton = new JButton("Bytes");
        bytesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showBytes();
            }
        });
        viewButtonsPanel.add(bytesButton);


        valuesPanel.add(viewButtonsPanel, NORTH);
        splitPane.setBottomComponent(valuesPanel);

        switch (property.getType()) {
            case TEXT:
            case IMAGE: {
                showValue();
                break;
            }
            case BYTES: {
                valueButton.setEnabled(false);
                showBytes();
            }
        }


        add(splitPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                thisDialog.dispose();
            }
        });
        add(closeButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void showBytes() {
        bytesButton.setEnabled(false);
        if (!property.getType().equals(Property.ValueType.BYTES)){
            valueButton.setEnabled(true);
        }
        removePreviousValue();

        JTextArea comp = new JTextArea(Arrays.toString(property.getBytes()));
        comp.setEditable(false);
        valuesPanel.add(new JScrollPane(comp), BorderLayout.CENTER);

        valuesPanel.revalidate();
        valuesPanel.repaint();
    }

    private void showValue() {
        valueButton.setEnabled(false);
        bytesButton.setEnabled(true);
        removePreviousValue();
        switch (property.getType()) {
            case TEXT: {
                JTextArea comp = new JTextArea(property.getValue().toString());
                comp.setEditable(false);
                valuesPanel.add(new JScrollPane(comp), BorderLayout.CENTER);
                break;
            }
            case IMAGE: {
                ImageIcon value = (ImageIcon) property.getValue();
                valuesPanel.add(new JScrollPane(new JLabel("", value, CENTER)), BorderLayout.CENTER);
            }
        }
        valuesPanel.revalidate();
        valuesPanel.repaint();
    }

    private void removePreviousValue(){
        Component[] components = valuesPanel.getComponents();
        for (Component c: components){
            if (c instanceof JScrollPane){
                valuesPanel.remove(c);
            }
        }
    }
}
