package com.wirusmx.ole2editor.application.view.gui;


import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;

public class DefaultExceptionsHandler {
    private static Logger logger = Logger.getLogger(DefaultExceptionsHandler.class);
    private static ResourceBundle uiResourceBundle = ResourceBundle.getBundle("lang.ui");

    private static boolean isLoggerReady = false;

    static {
        Properties loggerProperties = new Properties();
        try {
            loggerProperties.load(new FileInputStream("cfg/log4j.properties"));
            PropertyConfigurator.configure(loggerProperties);
            isLoggerReady = true;
        } catch (IOException ignored) {

        }
    }

    public static void handle(JFrame parent, Throwable ex){
        if (isLoggerReady){
            logger.error(ex.getMessage(), ex);
        }

        if (parent != null) {
            JOptionPane.showMessageDialog(parent,
                    uiResourceBundle.getString("dialog_error_text") + " " + ex.getLocalizedMessage(),
                    uiResourceBundle.getString("dialog_error_header"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
