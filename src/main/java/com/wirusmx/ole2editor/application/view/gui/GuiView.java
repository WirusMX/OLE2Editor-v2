package com.wirusmx.ole2editor.application.view.gui;

import com.wirusmx.ole2editor.application.controller.Controller;
import com.wirusmx.ole2editor.application.view.FileChooser;
import com.wirusmx.ole2editor.application.view.View;
import com.wirusmx.ole2editor.application.view.gui.actions.ExitAction;
import com.wirusmx.ole2editor.application.view.gui.actions.OpenAction;
import com.wirusmx.ole2editor.application.view.gui.actions.ShowHidePanelAction;
import com.wirusmx.ole2editor.application.view.gui.listeners.SaveMenuListener;
import com.wirusmx.ole2editor.application.view.gui.listeners.ViewMenuListener;
import com.wirusmx.ole2editor.application.view.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class GuiView extends JFrame implements View {
    private ResourceBundle uiResourceBundle = ResourceBundle.getBundle("lang.ui");

    private final String frameName = "OLE2 EDITOR";

    private Controller controller;

    private JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    private SectorsPanel sectorsPanel = null;
    private StreamsPanel streamsPanel = new StreamsPanel(this);
    private SystemInformationPanel systemInformationPanel = null;

    private HexEditorPanel hexEditorPanel = null;
    private FilesPanel filesPanel = new FilesPanel(this);
    private PropertiesPanel propertiesPanel = null;

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
    public int getVisiblePanelsCode() {
        int result = 0;

        if (splitPane2.getLeftComponent() != null) {
            if (splitPane2.getLeftComponent() instanceof SectorsPanel) {
                result += 1;
            }

            if (splitPane2.getLeftComponent() instanceof StreamsPanel) {
                result += 2;
            }

            if (splitPane2.getLeftComponent() instanceof SystemInformationPanel) {
                result += 4;
            }
        }

        if (splitPane2.getRightComponent() != null) {
            if (splitPane2.getRightComponent() instanceof HexEditorPanel) {
                result += 8;
            }

            if (splitPane2.getRightComponent() instanceof FilesPanel) {
                result += 16;
            }

            if (splitPane2.getRightComponent() instanceof PropertiesPanel) {
                result += 32;
            }
        }

        return result;
    }

    @Override
    public void hidePanel(Class panelClass) {
        if (panelClass == SectorsPanel.class
                || panelClass == StreamsPanel.class
                || panelClass == SystemInformationPanel.class) {

            splitPane2.setLeftComponent(null);
        }

        if (panelClass == HexEditorPanel.class
                || panelClass == FilesPanel.class
                || panelClass == PropertiesPanel.class) {

            splitPane2.setRightComponent(null);
        }
    }

    @Override
    public void showPanel(Class panelClass) {
        if (panelClass == SectorsPanel.class){
            splitPane2.setLeftComponent(new SectorsPanel(this));
        }

        if (panelClass == StreamsPanel.class){
            splitPane2.setLeftComponent(new StreamsPanel(this));
        }

        if (panelClass == SystemInformationPanel.class){
            splitPane2.setLeftComponent(new SystemInformationPanel(this));
        }

        if (panelClass == HexEditorPanel.class){
            splitPane2.setRightComponent(new HexEditorPanel(this));
        }

        if (panelClass == FilesPanel.class){
            splitPane2.setRightComponent(new FilesPanel(this));
        }

        if (panelClass == PropertiesPanel.class){
            splitPane2.setRightComponent(new PropertiesPanel(this));
        }
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

    public void update() {
        if (streamsPanel != null) {
            streamsPanel.update();
        }

        if (filesPanel != null) {
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
        JCheckBoxMenuItem sectorsPanel = addCheckBoxMenuItem(leftPanel,
                uiResourceBundle.getString("menu_view_left_panel_sectors"),
                false,
                new ShowHidePanelAction(this, SectorsPanel.class));
        JCheckBoxMenuItem streamsPanel = addCheckBoxMenuItem(leftPanel,
                uiResourceBundle.getString("menu_view_left_panel_streams"),
                false,
                new ShowHidePanelAction(this, StreamsPanel.class));
        JCheckBoxMenuItem systemInformationPanel = addCheckBoxMenuItem(leftPanel,
                uiResourceBundle.getString("menu_view_left_panel_system_information"),
                false,
                new ShowHidePanelAction(this, SystemInformationPanel.class));

        JMenu rightPanel = new JMenu(uiResourceBundle.getString("menu_view_right_panel"));
        viewMenu.add(rightPanel);
        JCheckBoxMenuItem hexPanel = addCheckBoxMenuItem(rightPanel,
                uiResourceBundle.getString("menu_view_right_panel_stream_hex"),
                false,
                new ShowHidePanelAction(this, HexEditorPanel.class));
        JCheckBoxMenuItem osfsPanel = addCheckBoxMenuItem(rightPanel,
                uiResourceBundle.getString("menu_view_right_panel_os_fs"),
                false,
                new ShowHidePanelAction(this, FilesPanel.class));
        JCheckBoxMenuItem streamPropertiesPanel = addCheckBoxMenuItem(rightPanel,
                uiResourceBundle.getString("menu_view_right_panel_stream_properties"),
                false,
                new ShowHidePanelAction(this, PropertiesPanel.class));

        viewMenu.addMenuListener(new ViewMenuListener(this, sectorsPanel, streamsPanel,
                systemInformationPanel, hexPanel, osfsPanel, streamPropertiesPanel));

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
