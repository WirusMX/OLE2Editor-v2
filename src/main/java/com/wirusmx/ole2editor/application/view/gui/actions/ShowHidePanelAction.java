package com.wirusmx.ole2editor.application.view.gui.actions;

import com.wirusmx.ole2editor.application.view.View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowHidePanelAction implements ActionListener{
    private Class panelClass;
    private View view;

    public ShowHidePanelAction(View view, Class panelClass) {
        this.panelClass = panelClass;
        this.view = view;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        JCheckBoxMenuItem menuItem = (JCheckBoxMenuItem) e.getSource();
        if (menuItem.isSelected()){
            view.showPanel(panelClass);
        } else {
            view.hidePanel(panelClass);
        }
    }
}
