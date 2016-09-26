package com.wirusmx.ole2editor.view;

import com.wirusmx.ole2editor.controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIView extends JFrame implements ActionListener {
    private final String frameName = "OLE2 EDITOR v2";

    private Controller controller;

    private JTextArea textArea = new JTextArea("");

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(frameName + " " + title);
    }

    public void init() {
        super.setTitle(frameName);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        initMainMenuBar();
        textArea.setVisible(true);
        getContentPane().add(textArea, BorderLayout.WEST);
        setVisible(true);
    }

    private void initMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        MenuHelper.initFileMenu(this, menuBar);
        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    private void updateText(String tree) {
        String text = controller.getCurrentFile().getName() + tree;

        textArea.setText(text);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "New": {
                break;
            }
            case "Open": {
                controller.openFile();
                break;
            }
            case "Save": {
                break;
            }
            case "Save as...": {
                break;
            }
            case "Exit": {
                controller.exit();
                break;
            }
        }
    }

    public void update() {
        updateText(controller.getStreamsTree());
    }
}
