package com.wirusmx.ole2editor;

import com.wirusmx.ole2editor.application.controller.Controller;
import com.wirusmx.ole2editor.application.model.Model;
import com.wirusmx.ole2editor.application.view.View;
import com.wirusmx.ole2editor.application.view.gui.GuiView;

import java.util.Locale;

/**
 * Application main class, contains public static void main method
 * and nothing else.
 */
public class MainClass {
    public static void main(String[] args) {
        Locale.setDefault(new Locale("en"));
        Model model = new Model();
        View view = new GuiView();

        Controller controller = new Controller(model, view);

        controller.init();
    }
}
