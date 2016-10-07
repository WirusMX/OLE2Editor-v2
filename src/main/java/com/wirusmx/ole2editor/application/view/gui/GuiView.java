package com.wirusmx.ole2editor.application.view.gui;

import com.wirusmx.ole2editor.application.controller.Controller;
import com.wirusmx.ole2editor.application.view.FileChooser;
import com.wirusmx.ole2editor.application.view.View;
import com.wirusmx.ole2editor.application.view.gui.actions.*;
import com.wirusmx.ole2editor.application.view.gui.listeners.SaveMenuListener;
import com.wirusmx.ole2editor.application.view.gui.panels.FilesPanel;
import com.wirusmx.ole2editor.application.view.gui.panels.StreamsPanel;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
public class GuiView extends JFrame implements View {
    private ResourceBundle uiResourceBundle = ResourceBundle.getBundle("lang.ui");

    private final String frameName = "OLE2 EDITOR";

    private Controller controller;

    private JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    private FilesPanel filesPanel = new FilesPanel(this);
    private StreamsPanel streamsPanel = new StreamsPanel(this);


    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public FileChooser getFileChooser() {
        return new GuiFileChooser(this);
    }

    @Override
    public Controller getController() {
        return controller;
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(frameName + " " + title);
    }

    public void init() {
        super.setTitle(frameName);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setExtendedState(MAXIMIZED_BOTH);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        initMainMenuBar();

        splitPane2.setLeftComponent(streamsPanel);

        splitPane2.setRightComponent(filesPanel);

        add(splitPane2, BorderLayout.CENTER);

        setVisible(true);
    }

    public void updateStreams(LinkedOLE2Entry tree){
        streamsPanel.updateList(tree);
    }

    private void initMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        initFileMenu(menuBar);
        initViewMenu(menuBar);
        initHelpMenu(menuBar);

        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    private void initFileMenu(JMenuBar menuBar) {
        JMenu fileMenu = new JMenu(uiResourceBundle.getString("menu_file"));
        menuBar.add(fileMenu);

        addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_new"), null);
        addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_open"), new OpenAction(this));
        JMenuItem menuSave = addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_save"), null);
        JMenuItem menuSaveAs = addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_save_as"), null);
        fileMenu.addMenuListener(new SaveMenuListener(this, menuSave, menuSaveAs));
        fileMenu.addSeparator();
        addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_exit"), new ExitAction(this));
    }

    private void initViewMenu(JMenuBar menuBar) {
        JMenu viewMenu = new JMenu(uiResourceBundle.getString("menu_view"));
        menuBar.add(viewMenu);

        JMenu leftPanel = new JMenu(uiResourceBundle.getString("menu_view_left_panel"));
        viewMenu.add(leftPanel);
        addCheckBoxMenuItem(leftPanel, uiResourceBundle.getString("menu_view_left_panel_tree"), true, null);
        addCheckBoxMenuItem(leftPanel, uiResourceBundle.getString("menu_view_left_panel_list"), true, null);

        JMenu rightPanel = new JMenu(uiResourceBundle.getString("menu_view_right_panel"));
        viewMenu.add(rightPanel);
        addCheckBoxMenuItem(rightPanel, uiResourceBundle.getString("menu_view_right_panel_stream_hex"), false, null);
        addCheckBoxMenuItem(rightPanel, uiResourceBundle.getString("menu_view_right_panel_os_fs"), true, null);
        addCheckBoxMenuItem(rightPanel, uiResourceBundle.getString("menu_view_right_panel_stream_properties"), false, null);

        viewMenu.addSeparator();

        addMenuItem(viewMenu, uiResourceBundle.getString("menu_view_save_view_settings"), null);
    }

    private void initHelpMenu(JMenuBar menuBar) {
        JMenu helpMenu = new JMenu(uiResourceBundle.getString("menu_help"));
        menuBar.add(helpMenu);

        addMenuItem(helpMenu, uiResourceBundle.getString("menu_help_about"), null);
    }

    private JMenuItem addMenuItem(JMenuItem parent, String text, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionListener);
        parent.add(menuItem);
        return menuItem;
    }

    private JCheckBoxMenuItem addCheckBoxMenuItem(JMenuItem parent, String text, boolean checked, ActionListener actionListener) {
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(text, checked);
        menuItem.addActionListener(actionListener);
        parent.add(menuItem);
        return menuItem;
    }

    public void update() {
    }


}
