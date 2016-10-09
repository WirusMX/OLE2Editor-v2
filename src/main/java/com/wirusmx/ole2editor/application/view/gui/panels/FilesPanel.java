package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.View;
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

public class FilesPanel extends JPanel {
    private DefaultListModel<JListElementWrapper> filesListModel = new DefaultListModel<>();
    private JList<JListElementWrapper> filesList = new JList<>(filesListModel);
    private JScrollPane filesListPane = new JScrollPane(filesList);
    private JLabel filesListLabel = new JLabel(" ");

    private View view;

    private File current = new File("").getAbsoluteFile();

    public FilesPanel(View view) {
        this.view = view;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        filesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filesList.addMouseListener(new GotoDirectory());
        filesList.setLayoutOrientation(JList.VERTICAL);

        updateFilesList(current);

        add(filesListLabel, BorderLayout.NORTH);
        add(filesListPane, BorderLayout.CENTER);
    }

    public void update(){
        updateFilesList(current);
    }

    public void updateFilesList(File file) {
        if (file == null || !file.exists()){
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
                filesListModel.addElement(new FileListElement(f));
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