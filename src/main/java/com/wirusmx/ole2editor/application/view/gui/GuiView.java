package com.wirusmx.ole2editor.application.view.gui;

import com.wirusmx.ole2editor.application.controller.Controller;
import com.wirusmx.ole2editor.application.view.FileChooser;
import com.wirusmx.ole2editor.application.view.View;
import com.wirusmx.ole2editor.application.view.gui.actions.*;
import com.wirusmx.ole2editor.application.view.gui.listeners.SaveMenuListener;
import com.wirusmx.ole2editor.application.view.gui.wrappers.FileListElement;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListElementWrapper;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListParentElement;
import com.wirusmx.ole2editor.application.view.gui.wrappers.StreamsListElement;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;

public class GuiView extends JFrame implements View {
    private ResourceBundle uiResourceBundle = ResourceBundle.getBundle("lang.ui");

    private final String frameName = "OLE2 EDITOR";

    private Controller controller;

    private JSplitPane splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    private int splitPaneDividerSize = splitPane1.getDividerSize();

    private DefaultListModel<JListElementWrapper> streamsListModel = new DefaultListModel<>();
    private JList<JListElementWrapper> streamsList = new JList<>(streamsListModel);
    private JScrollPane streamsListPane = new JScrollPane(streamsList);

    private DefaultListModel<JListElementWrapper> filesListModel = new DefaultListModel<>();
    private JList<JListElementWrapper> filesList = new JList<>(filesListModel);
    private JScrollPane filesListPane = new JScrollPane(filesList);

    private JLabel status1 = new JLabel();
    private JLabel status2 = new JLabel();

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

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(status1, BorderLayout.NORTH);
        panel.add(streamsListPane, BorderLayout.CENTER);
        splitPane1.setRightComponent(panel);

        splitPane2.setLeftComponent(splitPane1);

        JPanel panel1 = new JPanel(new BorderLayout());
        panel1.add(status2, BorderLayout.NORTH);
        panel1.add(filesListPane, BorderLayout.CENTER);
        splitPane2.setRightComponent(panel1);

        add(splitPane2, BorderLayout.CENTER);

        streamsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        streamsList.addMouseListener(new GotoStorage(this));
        streamsList.setLayoutOrientation(JList.VERTICAL);

        filesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filesList.addMouseListener(new GotoDirectory(this));
        filesList.setLayoutOrientation(JList.VERTICAL);
        updateFilesList(new File("").getAbsoluteFile());

        setVisible(true);
    }

    public void updateStatus1(String value) {
        status1.setText(value);
    }

    public void updateStatus2(String value) {
        status2.setText(value);
    }

    public void updateFilesList(File file) {
        updateStatus2(file.getAbsolutePath());
        filesListModel.clear();
        if (file.getParentFile() != null) {
            filesListModel.addElement(new JListParentElement<>(file.getParentFile()));
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) {
                return;
            }
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    if (o1 == null) {
                        return 1;
                    }

                    if (o2 == null) {
                        return -1;
                    }

                    if (o1.isDirectory()) {
                        if (!o2.isDirectory()) {
                            return -1;
                        }
                    }

                    if (!o1.isDirectory()) {
                        if (o2.isDirectory()) {
                            return 1;
                        }
                    }

                    return o1.getName().compareTo(o2.getName());
                }
            });

            for (File f : files) {
                filesListModel.addElement(new FileListElement(f));
            }
        }
    }

    public void updateStreamsList(LinkedOLE2Entry tree) {
        streamsListModel.clear();
        if (tree.getParent() != null) {
            streamsListModel.addElement(new JListParentElement<>(tree.getParent()));
        }

        for (LinkedOLE2Entry e : tree.entriesList()) {
            streamsListModel.addElement(new StreamsListElement(e));
        }
    }

    public boolean isStreamsTreeViewHide() {
        return splitPane1.getLeftComponent() == null;
    }

    public void hideStreamsTreeView() {
        splitPane1.setLeftComponent(null);
        splitPane1.setDividerSize(0);
    }

    public void showStreamsTreeView() {
        splitPane1.setDividerSize(splitPaneDividerSize);
    }

    public boolean isStreamsListViewHide() {
        return splitPane1.getRightComponent() == null;
    }

    public void hideStreamsListView() {
        splitPane1.setRightComponent(null);
        splitPane2.setDividerSize(0);
    }

    public void showStreamsListView() {
        splitPane2.setDividerSize(splitPaneDividerSize);
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
        addCheckBoxMenuItem(leftPanel, uiResourceBundle.getString("menu_view_left_panel_tree"), true, new HideStreamsTreeView(this));
        addCheckBoxMenuItem(leftPanel, uiResourceBundle.getString("menu_view_left_panel_list"), true, new HideStreamsListView(this));

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
