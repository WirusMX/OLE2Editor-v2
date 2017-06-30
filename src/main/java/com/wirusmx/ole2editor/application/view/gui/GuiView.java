package com.wirusmx.ole2editor.application.view.gui;

import com.wirusmx.ole2editor.application.controller.Controller;
import com.wirusmx.ole2editor.application.view.gui.actions.ExitAction;
import com.wirusmx.ole2editor.application.view.gui.actions.OpenAction;
import com.wirusmx.ole2editor.application.view.gui.actions.ShowHidePanelAction;
import com.wirusmx.ole2editor.application.view.gui.listeners.PanelListener;
import com.wirusmx.ole2editor.application.view.gui.listeners.SaveMenuListener;
import com.wirusmx.ole2editor.application.view.gui.listeners.ViewMenuListener;
import com.wirusmx.ole2editor.application.view.gui.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

public class GuiView extends JFrame {
    private ResourceBundle uiResourceBundle = ResourceBundle.getBundle("lang.ui");

    private final String FRAME_NAME = "OLE2 EDITOR 2.0";

    private Controller controller;

    private JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(FRAME_NAME + " " + title);
    }

    public int getVisiblePanelsCode() {
        int result = 0;

        if (splitPane.getLeftComponent() != null) {
            if (splitPane.getLeftComponent() instanceof SectorsPanel) {
                result += 1;
            }

            if (splitPane.getLeftComponent() instanceof StreamsPanel) {
                result += 2;
            }
        }

        if (splitPane.getRightComponent() != null) {
            if (splitPane.getRightComponent() instanceof HexEditorPanel) {
                result += 8;
            }

            if (splitPane.getRightComponent() instanceof FilesPanel) {
                result += 16;
            }

            if (splitPane.getRightComponent() instanceof PropertiesPanel) {
                result += 32;
            }
        }

        return result;
    }

    public void hidePanel(Class panelClass) {
        if (panelClass == SectorsPanel.class
                || panelClass == StreamsPanel.class) {

            splitPane.setLeftComponent(null);
        }

        if (panelClass == HexEditorPanel.class
                || panelClass == FilesPanel.class
                || panelClass == PropertiesPanel.class) {

            splitPane.setRightComponent(null);
        }
    }


    public void showPanel(Class panelClass) {
        MyPanel panel = null;
        if (panelClass == SectorsPanel.class) {
            panel = new SectorsPanel(this);
            splitPane.setLeftComponent(panel);
        }

        if (panelClass == StreamsPanel.class) {
            panel = new StreamsPanel(this);
            splitPane.setLeftComponent(panel);
        }

        if (panelClass == HexEditorPanel.class) {
            panel = new HexEditorPanel(this);
            splitPane.setRightComponent(panel);
        }

        if (panelClass == FilesPanel.class) {
            panel = new FilesPanel(this);
            splitPane.setRightComponent(panel);
        }

        if (panelClass == PropertiesPanel.class) {
            panel = new PropertiesPanel(this);
            splitPane.setRightComponent(panel);
        }

        if (panel != null){
            panel.init();
            panel.update();
        }
    }

    public void init() {
        super.setTitle(FRAME_NAME);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new ExitAction(this));
        setSize(800, 600);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        initMainMenuBar();
        addActionButtons();
        add(splitPane, BorderLayout.CENTER);
        splitPane.setLeftComponent(new StreamsPanel(this));
        splitPane.setRightComponent(new FilesPanel(this));
        splitPane.setDividerSize(10);
        splitPane.addComponentListener(new PanelListener(splitPane));

        setVisible(true);
        setExtendedState(MAXIMIZED_BOTH);
    }

    private void addActionButtons() {
        Panel panel = new Panel(new GridLayout(1, 7));
        JButton actionsViewButton = new JButton("F3  " + uiResourceBundle.getString("menu_actions_view"));
        actionsViewButton.addActionListener(new ShowHidePanelAction(this, HexEditorPanel.class));
        panel.add(actionsViewButton);
        panel.add(new JButton("F4  " + uiResourceBundle.getString("menu_actions_stream")));
        panel.add(new JButton("F5  " + uiResourceBundle.getString("menu_actions_copy")));
        panel.add(new JButton("F6  " + uiResourceBundle.getString("menu_actions_move")));
        panel.add(new JButton("F7  " + uiResourceBundle.getString("menu_actions_storage")));
        panel.add(new JButton("F8  " + uiResourceBundle.getString("menu_actions_remove")));
        JButton exitButton = new JButton("Alt+X  " + uiResourceBundle.getString("menu_file_exit"));
        exitButton.addActionListener(new ExitAction(this));
        panel.add(exitButton);
        add(panel, BorderLayout.SOUTH);
    }

    public void update() {
        updatePanel((MyPanel) splitPane.getRightComponent());
        updatePanel((MyPanel) splitPane.getLeftComponent());
    }

    private void updatePanel(MyPanel panel) {
        if (panel != null){
            panel.update();
        }
    }


    public void reset(){
        splitPane.setDividerLocation(0.5);
        MyPanel left = (MyPanel) splitPane.getLeftComponent();
        if (left != null){
            left.reset();
        }

        MyPanel right = (MyPanel) splitPane.getRightComponent();
        if (right != null){
            right.reset();
        }

    }

    private void initMainMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        initFileMenu(menuBar);
        initActionsMenu(menuBar);
        initViewMenu(menuBar);
        initHelpMenu(menuBar);

        getContentPane().add(menuBar, BorderLayout.NORTH);
    }

    private void initFileMenu(JMenuBar menuBar) {
        JMenu fileMenu = new JMenu(uiResourceBundle.getString("menu_file"));
        menuBar.add(fileMenu);

        addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_new"), null, "file.png", KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_open"), new OpenAction(this), "menu_open.png", KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        JMenuItem menuSave = addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_save"), null, "menu_save.png", KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        JMenuItem menuSaveAs = addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_save_as"), null, "menu_saveas.png", KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        fileMenu.addMenuListener(new SaveMenuListener(this, menuSave, menuSaveAs));
        fileMenu.addSeparator();
        addMenuItem(fileMenu, uiResourceBundle.getString("menu_file_exit"), new ExitAction(this), KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
    }

    private void initActionsMenu(JMenuBar menuBar){
        JMenu actionsMenu = new JMenu(uiResourceBundle.getString("menu_actions"));
        menuBar.add(actionsMenu);

        addMenuItem(actionsMenu, uiResourceBundle.getString("menu_actions_view"),
                new ShowHidePanelAction(this, HexEditorPanel.class),
                KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        addMenuItem(actionsMenu, uiResourceBundle.getString("menu_actions_stream"), null, KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
        addMenuItem(actionsMenu, uiResourceBundle.getString("menu_actions_copy"), null, KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        addMenuItem(actionsMenu, uiResourceBundle.getString("menu_actions_move"), null, KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
        addMenuItem(actionsMenu, uiResourceBundle.getString("menu_actions_storage"), null, KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
        addMenuItem(actionsMenu, uiResourceBundle.getString("menu_actions_remove"), null, KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));

    }

    private void initViewMenu(JMenuBar menuBar) {
        JMenu viewMenu = new JMenu(uiResourceBundle.getString("menu_view"));
        menuBar.add(viewMenu);

        JMenu leftPanel = new JMenu(uiResourceBundle.getString("menu_view_left_panel"));
        viewMenu.add(leftPanel);
        JCheckBoxMenuItem sectorsPanel = addCheckBoxMenuItem(leftPanel,
                uiResourceBundle.getString("menu_view_left_panel_sectors"),
                false,
                new ShowHidePanelAction(this, SectorsPanel.class),
                KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.CTRL_MASK));
        JCheckBoxMenuItem streamsPanel = addCheckBoxMenuItem(leftPanel,
                uiResourceBundle.getString("menu_view_left_panel_streams"),
                false,
                new ShowHidePanelAction(this, StreamsPanel.class),
                KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.CTRL_MASK));

        JMenu rightPanel = new JMenu(uiResourceBundle.getString("menu_view_right_panel"));
        viewMenu.add(rightPanel);
        JCheckBoxMenuItem hexPanel = addCheckBoxMenuItem(rightPanel,
                uiResourceBundle.getString("menu_view_right_panel_stream_hex"),
                false,
                new ShowHidePanelAction(this, HexEditorPanel.class),
                KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        JCheckBoxMenuItem osfsPanel = addCheckBoxMenuItem(rightPanel,
                uiResourceBundle.getString("menu_view_right_panel_os_fs"),
                false,
                new ShowHidePanelAction(this, FilesPanel.class),
                KeyStroke.getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        JCheckBoxMenuItem streamPropertiesPanel = addCheckBoxMenuItem(rightPanel,
                uiResourceBundle.getString("menu_view_right_panel_stream_properties"),
                false,
                new ShowHidePanelAction(this, PropertiesPanel.class),
                KeyStroke.getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));

        viewMenu.addMenuListener(new ViewMenuListener(this, sectorsPanel, streamsPanel,
                hexPanel, osfsPanel, streamPropertiesPanel));

        viewMenu.addSeparator();

        addMenuItem(viewMenu, uiResourceBundle.getString("menu_view_save_view_settings"), null);
    }

    private void initHelpMenu(JMenuBar menuBar) {
        JMenu helpMenu = new JMenu(uiResourceBundle.getString("menu_help"));
        menuBar.add(helpMenu);

        addMenuItem(helpMenu, uiResourceBundle.getString("menu_help_about"), null, "menu_about.png");
    }

    private JMenuItem addMenuItem(JMenuItem parent, String text, ActionListener actionListener,
                                  String imageName) {

        return addMenuItem(parent, text, actionListener, imageName, null);

    }

    private JMenuItem addMenuItem(JMenuItem parent, String text, ActionListener actionListener) {
        return addMenuItem(parent, text, actionListener, null, null);
    }

    private JMenuItem addMenuItem(JMenuItem parent, String text, ActionListener actionListener,
                                  KeyStroke keyStroke) {
        return addMenuItem(parent, text, actionListener, null, keyStroke);
    }

    private JMenuItem addMenuItem(JMenuItem parent, String text, ActionListener actionListener,
                                  String imageName, KeyStroke keyStroke) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionListener);
        if (imageName != null) {

            ImageIcon icon = ImageLoader.load(imageName);
            if (icon != null) {
                menuItem.setIcon(icon);
            }
        }

        if (keyStroke != null) {
            menuItem.setAccelerator(keyStroke);
        }

        parent.add(menuItem);
        return menuItem;
    }

    private JCheckBoxMenuItem addCheckBoxMenuItem(JMenuItem parent, String text, boolean checked,
                                                  ActionListener actionListener, KeyStroke keyStroke) {
        JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem(text, checked);
        menuItem.addActionListener(actionListener);

        if (keyStroke != null) {
            menuItem.setAccelerator(keyStroke);
        }

        parent.add(menuItem);
        return menuItem;
    }
}
