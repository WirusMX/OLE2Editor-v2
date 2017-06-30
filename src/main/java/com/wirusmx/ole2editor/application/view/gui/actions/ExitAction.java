package com.wirusmx.ole2editor.application.view.gui.actions;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ResourceBundle;

public class ExitAction implements ActionListener, WindowListener {
    private GuiView view;

    public ExitAction(GuiView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ResourceBundle uiResourceBundle = ResourceBundle.getBundle("lang.ui");
        int dialogResult = JOptionPane.showOptionDialog(view,
                uiResourceBundle.getString("dialog_exit_question_text"),
                uiResourceBundle.getString("dialog_question_header"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{uiResourceBundle.getString("dialog_yes"), uiResourceBundle.getString("dialog_no")},
                uiResourceBundle.getString("dialog_no"));

        if (dialogResult == JOptionPane.YES_OPTION){
            view.getController().exit();
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        actionPerformed(null);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
