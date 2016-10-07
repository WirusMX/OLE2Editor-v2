package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.View;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListElementWrapper;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListParentElement;
import com.wirusmx.ole2editor.application.view.gui.wrappers.StreamsListElement;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class StreamsPanel extends JPanel {
    private JSplitPane splitPane = new JSplitPane();

    private JScrollPane streamsTreeScrollPane = new JScrollPane();

    private DefaultListModel<JListElementWrapper> streamsListModel = new DefaultListModel<>();
    private JList<JListElementWrapper> streamsList = new JList<>(streamsListModel);
    private JScrollPane streamsListScrollPane = new JScrollPane(streamsList);
    private JLabel streamsListLabel = new JLabel(" ");
    private JPanel rightPanel = new JPanel(new BorderLayout());

    private View parent;

    public StreamsPanel(View parent) {
        this.parent = parent;
        init();
    }

    public void init() {
        setLayout(new BorderLayout());

        splitPane.setLeftComponent(streamsTreeScrollPane);

        streamsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        streamsList.addMouseListener(new GotoStorage());
        streamsList.setLayoutOrientation(JList.VERTICAL);
        rightPanel.add(streamsListLabel, BorderLayout.NORTH);
        rightPanel.add(streamsListScrollPane, BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);
    }

    public void updateList(LinkedOLE2Entry tree){
        streamsListLabel.setText(tree.getAbsolutePath());
        streamsListModel.clear();
        if (tree.getParent() != null) {
            streamsListModel.addElement(new JListParentElement<>(tree.getParent()));
        }

        for (LinkedOLE2Entry e : tree.entriesList()) {
            streamsListModel.addElement(new StreamsListElement(e));
        }
    }

    class GotoStorage implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() != 2){
                return;
            }

            JList<JListElementWrapper<LinkedOLE2Entry>> list = (JList<JListElementWrapper<LinkedOLE2Entry>>) e.getSource();
            LinkedOLE2Entry tree = list.getSelectedValue().getObject();

            if (tree == null){
                return;
            }

            if (tree.getChild() != null) {
                updateList(tree);

                streamsListLabel.setText(tree.getAbsolutePath());
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
