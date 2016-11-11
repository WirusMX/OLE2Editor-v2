package com.wirusmx.ole2editor;

import com.wirusmx.ole2editor.application.controller.Controller;
import com.wirusmx.ole2editor.application.model.Model;
import com.wirusmx.ole2editor.application.view.gui.GuiView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * Application main class, contains public static void main method
 * and nothing else.
 */
public class MainClass {
    private static Properties applicationProperties = new Properties();

    private static final String APPLICATION_PROPERTIES_FILE = "cfg/o2e.properties";

    private static boolean loadProperties(){
        try {
            applicationProperties.load(new FileInputStream(APPLICATION_PROPERTIES_FILE));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String locale = "en";

        if (loadProperties()){
            locale = applicationProperties.getProperty("locale");
        }

        Locale.setDefault(new Locale(locale));
        Model model = new Model();
        GuiView view = new GuiView();

        Controller controller = new Controller(model, view);

        controller.init();
    }
}
