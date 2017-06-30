package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.GuiView;
import com.wirusmx.ole2editor.application.view.gui.ImageLoader;
import com.wirusmx.ole2editor.application.view.gui.wrappers.FileListElement;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListElementWrapper;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListParentElement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;

public class FilesPanel extends MyPanel {
    private ResourceBundle uiResourceBundle = ResourceBundle.getBundle("lang.ui");

    private DefaultListModel<JListElementWrapper> filesListModel;
    private JLabel filesListLabel;

    private File current;

    public FilesPanel(GuiView view) {
        super(view);
    }

    public void init() {
        setLayout(new BorderLayout());

        filesListModel = new DefaultListModel<>();
        JList<JListElementWrapper> filesList = new JList<>(filesListModel);
        JScrollPane filesListPane = new JScrollPane(filesList);
        filesListLabel = new JLabel(" ");

        filesList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof FileListElement) {
                    File file = ((FileListElement) value).getObject();
                    if (file.isDirectory()) {
                        label.setIcon(ImageLoader.load("folder.png"));
                        label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));
                    } else {
                        label.setIcon(ImageLoader.loadByExtension(file.getName(), false));
                    }
                }
                return label;
            }
        });
        filesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filesList.addMouseListener(new GotoDirectory());
        filesList.setLayoutOrientation(JList.VERTICAL);

        current = new File("").getAbsoluteFile();
        updateFilesList(current);

        add(filesListLabel, BorderLayout.NORTH);
        add(filesListPane, BorderLayout.CENTER);
    }

    @Override
    public void update() {
        updateFilesList(current);
    }

    @Override
    public void reset() {

    }

    public void updateFilesList(File file) {
        if (file == null || !file.exists()) {
            return;
        }

        current = file;

        filesListLabel.setText(file.getAbsolutePath());
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
                if (!f.isHidden()) {
                    filesListModel.addElement(new FileListElement(f));
                }
            }
        }
    }

    class GotoDirectory implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() != 2) {
                return;
            }

            JList<JListElementWrapper<File>> list = (JList<JListElementWrapper<File>>) e.getSource();
            File file = list.getSelectedValue().getObject();

            if (file == null) {
                return;
            }

            if (file.isDirectory()) {
                updateFilesList(file);
                filesListLabel.setText(file.getAbsolutePath());
            } else {
                int dialogResult = JOptionPane.showOptionDialog(view,
                        String.format(uiResourceBundle.getString("dialog_open_question_text"), file.getName()),
                        uiResourceBundle.getString("dialog_question_header"),
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new Object[]{uiResourceBundle.getString("dialog_yes"), uiResourceBundle.getString("dialog_no")},
                        uiResourceBundle.getString("dialog_yes"));

                if (dialogResult == JOptionPane.YES_OPTION) {
                    view.getController().openFile(file);
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}