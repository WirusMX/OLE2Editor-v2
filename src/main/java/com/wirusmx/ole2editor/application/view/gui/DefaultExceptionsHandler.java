package com.wirusmx.ole2editor.application.view.gui;


import javax.swing.*;

public class DefaultExceptionsHandler {
    private DefaultExceptionsHandler instance;

    private DefaultExceptionsHandler() {
    }

    public DefaultExceptionsHandler getInstance() {
        if (instance == null){
            instance = new DefaultExceptionsHandler();
        }

        return instance;
    }

    public static void handle(JFrame parent, Throwable ex){
        JOptionPane.showMessageDialog(parent, ex.getLocalizedMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
