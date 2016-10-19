package com.wirusmx.ole2editor.application.view.gui.listeners;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Created by wirusmx on 19.10.16.
 */
public class PanelListener implements ComponentListener {
    JSplitPane pane;

    public PanelListener(JSplitPane pane) {
        this.pane = pane;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        pane.setDividerLocation(0.5);
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
