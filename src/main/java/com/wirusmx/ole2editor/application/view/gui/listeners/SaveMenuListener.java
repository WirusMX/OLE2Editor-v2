package com.wirusmx.ole2editor.application.view.gui.listeners;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class SaveMenuListener implements MenuListener {
    private GuiView view;
    private JMenuItem saveMenuItem;
    private  JMenuItem saveAsMenuItem;

    public SaveMenuListener(GuiView view, JMenuItem saveMenuItem, JMenuItem saveAsMenuItem) {
        this.view = view;
        this.saveMenuItem = saveMenuItem;
        this.saveAsMenuItem = saveAsMenuItem;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        saveMenuItem.setEnabled(view.getController().isFileModified());
        saveAsMenuItem.setEnabled(view.getController().isFilePresent());
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
