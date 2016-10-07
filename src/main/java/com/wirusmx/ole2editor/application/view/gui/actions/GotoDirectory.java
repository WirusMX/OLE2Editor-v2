package com.wirusmx.ole2editor.application.view.gui.actions;

import com.wirusmx.ole2editor.application.view.View;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListElementWrapper;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

public class GotoDirectory implements MouseListener {
    private View view;

    public GotoDirectory(View view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2){
            return;
        }

        JList<JListElementWrapper<File>> list = (JList<JListElementWrapper<File>>) e.getSource();
        File file = list.getSelectedValue().getObject();

        if (file == null){
            return;
        }

        if (file.isDirectory()) {
            view.updateFilesList(file);
            view.updateStatus2(file.getAbsolutePath());
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
