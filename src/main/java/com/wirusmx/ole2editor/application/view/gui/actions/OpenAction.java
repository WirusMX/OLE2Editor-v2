package com.wirusmx.ole2editor.application.view.gui.actions;

import com.wirusmx.ole2editor.application.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenAction implements ActionListener{
    private View view;

    public OpenAction(View view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.getController().openFile();
    }
}
