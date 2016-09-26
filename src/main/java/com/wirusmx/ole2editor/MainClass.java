package com.wirusmx.ole2editor;

import com.wirusmx.ole2editor.controller.ConsoleController;
import com.wirusmx.ole2editor.controller.Controller;
import com.wirusmx.ole2editor.model.Model;
import com.wirusmx.ole2editor.view.ConsoleView;
import com.wirusmx.ole2editor.view.GUIView;

/**
 * Application main class, contains public static void main method
 * and nothing else.
 */
public class MainClass {
    public static void main(String[] args) {
        Model model = new Model();
        ConsoleView view = new ConsoleView();
        ConsoleController controller = new ConsoleController(model, view);

        controller.init();
    }
}
