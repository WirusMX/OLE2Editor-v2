package com.wirusmx.ole2editor.view;

import javax.swing.*;
import java.awt.event.ActionListener;

class MenuHelper {
    private static JMenuItem addMenuItem(JMenu parent, String text, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionListener);
        parent.add(menuItem);
        return menuItem;
    }

    static void initFileMenu(GUIView view, JMenuBar menuBar) {
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        addMenuItem(fileMenu, "New", view);
        addMenuItem(fileMenu, "Open", view);
        addMenuItem(fileMenu, "Save", view);
        addMenuItem(fileMenu, "Save as...", view);
        fileMenu.addSeparator();
        addMenuItem(fileMenu, "Exit", view);
    }
}
