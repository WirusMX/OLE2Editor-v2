package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.DefaultExceptionsHandler;
import com.wirusmx.ole2editor.application.view.gui.GuiView;
import com.wirusmx.ole2editor.application.view.gui.ImageLoader;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListElementWrapper;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListParentElement;
import com.wirusmx.ole2editor.application.view.gui.wrappers.StreamsListElement;
import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.io.OLE2Entry;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class StreamsPanel extends MyPanel {
    private JSplitPane splitPane;

    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JTree streamsTree;
    private JScrollPane streamsTreeScrollPane;

    private DefaultListModel<JListElementWrapper> streamsListModel;
    private JList<JListElementWrapper> streamsList;
    private JScrollPane streamsListScrollPane;
    private JLabel streamsListLabel;
    private JPanel rightPanel;


    private LinkedOLE2Entry tree = null;
    private LinkedOLE2Entry current = null;

    public StreamsPanel(GuiView view) {
        super(view);
    }

    public void init() {
        setLayout(new BorderLayout());

        splitPane = new JSplitPane();

        root = new DefaultMutableTreeNode("#root");
        treeModel = new DefaultTreeModel(root);
        streamsTree = new JTree(treeModel);
        streamsTreeScrollPane = new JScrollPane(streamsTree);

        ImageIcon folderIcon = ImageLoader.load("folder.png");
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(folderIcon);
        renderer.setOpenIcon(folderIcon);
        renderer.setClosedIcon(folderIcon);
        streamsTree.setCellRenderer(renderer);
        streamsTree.setRootVisible(false);
        streamsTree.setShowsRootHandles(true);
        streamsTree.addMouseListener(new GotoStorage());
        splitPane.setLeftComponent(streamsTreeScrollPane);

        streamsListModel = new DefaultListModel<>();
        streamsList = new JList<>(streamsListModel);
        streamsListScrollPane = new JScrollPane(streamsList);
        streamsListLabel = new JLabel(" ");
        rightPanel = new JPanel(new BorderLayout());
        streamsList.setCellRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof StreamsListElement) {
                    LinkedOLE2Entry entry = ((StreamsListElement) value).getObject();
                    if (entry.isStorage()) {
                        label.setIcon(ImageLoader.load("folder.png"));
                    } else {
                        label.setIcon(ImageLoader.load("stream.png"));
                    }
                }
                return label;
            }
        });
        streamsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        streamsList.addMouseListener(new GotoStorage());
        streamsList.setLayoutOrientation(JList.VERTICAL);
        rightPanel.add(streamsListLabel, BorderLayout.NORTH);
        rightPanel.add(streamsListScrollPane, BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);

        splitPane.setDividerSize(4);
        add(splitPane, BorderLayout.CENTER);
    }

    public void update() {
        if (tree == null) {
            try {
                tree = view.getController().getStreamsTree();
                updateTree(root, tree);
                updateList(tree);
            } catch (Exception e) {
                DefaultExceptionsHandler.handle(view, e);
            }
        }
    }

    private void updateTree(DefaultMutableTreeNode root, LinkedOLE2Entry tree) {
        if (tree == null) {
            return;
        }

        if (!tree.getType().equals(OLE2Entry.EntryType.ROOT_STORAGE)
                && !tree.getType().equals(OLE2Entry.EntryType.USER_STORAGE)) {
            return;
        }

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(tree);
        treeModel.insertNodeInto(node, root, root.getChildCount());

        java.util.List<LinkedOLE2Entry> entries = tree.entriesList();
        if (entries != null) {
            for (LinkedOLE2Entry e : entries) {
                if (e.getType().equals(OLE2Entry.EntryType.USER_STORAGE)) {
                    DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(e);
                    treeModel.insertNodeInto(newChild, node, node.getChildCount());
                    streamsTree.scrollPathToVisible(new TreePath(newChild.getPath()));
                    updateTree(newChild, e.getChild());
                }
            }
        }

    }

    private void updateList(LinkedOLE2Entry tree) {
        current = tree;
        streamsListLabel.setText(tree.getAbsolutePath());
        streamsListModel.clear();
        if (tree.getParent() != null) {
            streamsListModel.addElement(new JListParentElement<>(tree.getParent()));
        }

        for (LinkedOLE2Entry e : tree.entriesList()) {
            streamsListModel.addElement(new StreamsListElement(e));
        }
    }

    private class GotoStorage implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            LinkedOLE2Entry tree = null;


            if (e.getSource() instanceof JTree) {
                JTree jTree = (JTree) e.getSource();
                DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                if (lastSelectedPathComponent != null &&
                        lastSelectedPathComponent.getUserObject() instanceof LinkedOLE2Entry) {
                    tree = (LinkedOLE2Entry) lastSelectedPathComponent.getUserObject();
                }
            }

            if (e.getSource() instanceof JList && e.getClickCount() == 2) {
                JList<JListElementWrapper<LinkedOLE2Entry>> list = (JList<JListElementWrapper<LinkedOLE2Entry>>) e.getSource();
                tree = list.getSelectedValue().getObject();
            }

            if (tree == null) {
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
