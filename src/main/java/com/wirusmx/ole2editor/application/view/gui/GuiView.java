package com.wirusmx.ole2editor.application.view.gui;

import com.wirusmx.ole2editor.application.controller.Controller;
import com.wirusmx.ole2editor.application.view.FileChooser;
import com.wirusmx.ole2editor.application.view.View;
import com.wirusmx.ole2editor.application.view.gui.actions.ExitAction;
import com.wirusmx.ole2editor.application.view.gui.actions.OpenAction;
import com.wirusmx.ole2editor.application.view.gui.listeners.SaveMenuListener;
import com.wirusmx.ole2editor.application.view.gui.listeners.ViewMenuListener;
import com.wirusmx.ole2editor.application.view.gui.panels.FilesPanel;
import com.wirusmx.ole2editor.application.view.gui.panels.StreamsPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class GuiView extends JFrame implements View {
    private ResourceBundle uiResourceBundle = ResourceBundle.getBundle("lang.ui");

    private final String frameName = "OLE2 EDITOR";

    private Controller controller;

    private JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    private JPanel sectorsPanel = null;
    private StreamsPanel streamsPanel = new StreamsPanel(this);
    private JPanel systemInformationPanel = null;

    private JPanel hexEditorPanel = null;
    private FilesPanel filesPanel = new FilesPanel(this);
    private JPanel propertiesPanel = null;

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

    @Override
    public int getVisiblePanels() {
        int result = 0;
        if (sectorsPanel != null){
            result += 1;
        }

        if (streamsPanel != null){
            result += 2;
        }

        if (systemInformationPanel != null){
            result += 4;
        }

        if (hexEditorPanel != null){
            result += 8;
        }

        if (filesPanel != null){
            result += 16;
        }

        if (propertiesPanel != null){
            result += 32;
        }

        return result;
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
        splitPane2.setDividerSize(10);
        add(splitPane2, BorderLayout.CENTER);

        setVisible(true);
    }

    public void update(){
        if (streamsPanel != null) {
            streamsPanel.update();
        }

        if (filesPanel != null){
            filesPanel.update();
        }
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
        JCheckBoxMenuItem sectorsPanel = addCheckBoxMenuItem(leftPanel, uiResourceBundle.getString("menu_view_left_panel_sectors"), false, null);
        JCheckBoxMenuItem streamsPanel = addCheckBoxMenuItem(leftPanel, uiResourceBundle.getString("menu_view_left_panel_streams"), true, null);
        JCheckBoxMenuItem systemInformationPanel = addCheckBoxMenuItem(leftPanel, uiResourceBundle.getString("menu_view_left_panel_system_information"), false, null);


        JMenu rightPanel = new JMenu(uiResourceBundle.getString("menu_view_right_panel"));
        viewMenu.add(rightPanel);
        JCheckBoxMenuItem hexPanel = addCheckBoxMenuItem(rightPanel, uiResourceBundle.getString("menu_view_right_panel_stream_hex"), false, null);
        JCheckBoxMenuItem osfsPanel = addCheckBoxMenuItem(rightPanel, uiResourceBundle.getString("menu_view_right_panel_os_fs"), true, null);
        JCheckBoxMenuItem streamPropertiesPanel = addCheckBoxMenuItem(rightPanel, uiResourceBundle.getString("menu_view_right_panel_stream_properties"), false, null);

        viewMenu.addMenuListener(new ViewMenuListener(this, sectorsPanel, streamsPanel, systemInformationPanel, hexPanel, osfsPanel, streamPropertiesPanel));
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
}
