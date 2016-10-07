package com.wirusmx.ole2editor.application.view.gui.actions;

import com.wirusmx.ole2editor.application.view.View;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListElementWrapper;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GotoStorage implements MouseListener {
    private View view;

    public GotoStorage(View view) {
        this.view = view;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() != 2){
            return;
        }

        JList<JListElementWrapper<LinkedOLE2Entry>> list = (JList<JListElementWrapper<LinkedOLE2Entry>>) e.getSource();
        LinkedOLE2Entry tree = list.getSelectedValue().getObject();

        if (tree == null){
            return;
        }

        if (tree.getChild() != null) {
            view.updateStreamsList(tree);
            String path = tree.getNameAsString();
            LinkedOLE2Entry t = tree.getParent();
            while (t != null){
                path = t.getNameAsString() + "/" + path;
                t = t.getParent();
            }
            view.updateStatus1(path);
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
