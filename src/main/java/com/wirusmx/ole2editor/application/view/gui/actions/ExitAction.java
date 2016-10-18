package com.wirusmx.ole2editor.application.view.gui.actions;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExitAction implements ActionListener {
    private GuiView view;

    public ExitAction(GuiView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        view.getController().exit();
    }
}
