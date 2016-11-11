package com.wirusmx.ole2editor.application.view.gui.panels;

import com.wirusmx.ole2editor.application.view.gui.DefaultExceptionsHandler;
import com.wirusmx.ole2editor.application.view.gui.GuiView;
import com.wirusmx.ole2editor.application.view.gui.ImageLoader;
import com.wirusmx.ole2editor.application.view.gui.listeners.PanelListener;
import com.wirusmx.ole2editor.application.view.gui.wrappers.FileListElement;
import com.wirusmx.ole2editor.application.view.gui.wrappers.JListParentElement;
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
import java.io.File;
import java.io.IOException;
import java.util.List;

public class StreamsPanel extends MyPanel {

    private JSplitPane splitPane;

    private DefaultMutableTreeNode root;
    private DefaultTreeModel treeModel;
    private JTree streamsTree;

    private DefaultListModel<Object> streamsListModel;
    private JLabel streamsListLabel;


    private LinkedOLE2Entry currentStorage = null;

    public StreamsPanel(GuiView view) {
        super(view);
    }

    public void init() {
        setLayout(new BorderLayout());

        splitPane = new JSplitPane();

        root = new DefaultMutableTreeNode("");
        treeModel = new DefaultTreeModel(root);
        streamsTree = new JTree(treeModel);
        JScrollPane streamsTreeScrollPane = new JScrollPane(streamsTree);

        streamsTree.setCellRenderer(new MyDefaultTreeCellRenderer());
        streamsTree.setRootVisible(true);
        streamsTree.setShowsRootHandles(true);
        streamsTree.addMouseListener(new MyMouseListener());
        splitPane.setLeftComponent(streamsTreeScrollPane);

        streamsListModel = new DefaultListModel<>();
        JList<Object> streamsList = new JList<>(streamsListModel);
        JScrollPane streamsListScrollPane = new JScrollPane(streamsList);
        streamsListLabel = new JLabel(" ");
        JPanel rightPanel = new JPanel(new BorderLayout());
        streamsList.setCellRenderer(new MyDefaultListCellRenderer());
        streamsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        streamsList.addMouseListener(new MyMouseListener());
        streamsList.setLayoutOrientation(JList.VERTICAL);
        rightPanel.add(streamsListLabel, BorderLayout.NORTH);
        rightPanel.add(streamsListScrollPane, BorderLayout.CENTER);
        splitPane.setRightComponent(rightPanel);
        splitPane.addComponentListener(new PanelListener(splitPane));

        splitPane.setDividerSize(4);
        add(splitPane, BorderLayout.CENTER);
    }

    public void update() {
        if (currentStorage == null) {
            try {
                currentStorage = view.getController().getStreamsTree();
                updateTree();
                updateList(currentStorage);
            } catch (Exception e) {
                DefaultExceptionsHandler.handle(view, e);
            }
        }
    }

    @Override
    public void reset() {
        currentStorage = null;
        root.removeAllChildren();
        root.setUserObject("");
        treeModel.reload();
        streamsTree.removeAll();

        streamsListModel.clear();

        streamsListLabel.setText("");

        splitPane.setDividerLocation(0.5);

        update();
    }

    private void updateTree() throws IOException, IllegalFileStructure {
        root.setUserObject(new FileListElement(view.getController().getCurrentFile()));
        updateTree(root, currentStorage);
        addWidowedStreams(view.getController().getWidowedStreamsList());
        treeModel.insertNodeInto(new DefaultMutableTreeNode(new SystemInformation("System information", -5)), root,
                root.getChildCount());
    }

    private void addWidowedStreams(List<LinkedOLE2Entry> widowedStreamsList) {
        if (widowedStreamsList.size() > 0) {
            treeModel.insertNodeInto(new DefaultMutableTreeNode(new WidowedStreams(widowedStreamsList)), root,
                    root.getChildCount());
        }
    }

    private void updateTree(DefaultMutableTreeNode parent, LinkedOLE2Entry newNode) {
        if (newNode == null) {
            return;
        }

        if (!newNode.isStorage()) {
            return;
        }

        DefaultMutableTreeNode newMutableTreeNode = new DefaultMutableTreeNode(newNode);
        treeModel.insertNodeInto(newMutableTreeNode, parent, parent.getChildCount());

        java.util.List<LinkedOLE2Entry> entries = newNode.entriesList();
        if (entries != null) {
            for (LinkedOLE2Entry e : entries) {
                if (e.getType().equals(OLE2Entry.EntryType.USER_STORAGE)) {
                    DefaultMutableTreeNode newChild = new DefaultMutableTreeNode(e);
                    treeModel.insertNodeInto(newChild, newMutableTreeNode, newMutableTreeNode.getChildCount());
                    streamsTree.scrollPathToVisible(new TreePath(newChild.getPath()));
                    updateTree(newChild, e.getChild());
                }
            }
        }

    }

    private void updateList() {
        streamsListLabel.setText("System information");
        streamsListModel.clear();

        streamsListModel.addElement(new SystemInformation("HEADER", -1));
        streamsListModel.addElement(new SystemInformation("MSAT", -2));
        streamsListModel.addElement(new SystemInformation("SAT", -3));
        streamsListModel.addElement(new SystemInformation("SSAT", -4));
    }

    private void updateList(LinkedOLE2Entry storage) {
        streamsListLabel.setText(storage.getAbsolutePath());
        streamsListModel.clear();
        if (storage.getParent() != null) {
            streamsListModel.addElement(new JListParentElement<>(storage.getParent()));
        }

        for (LinkedOLE2Entry e : storage.entriesList()) {
            streamsListModel.addElement(e);
        }
    }

    private void updateList(List<LinkedOLE2Entry> streams) {
        streamsListLabel.setText("Widowed streams");
        streamsListModel.clear();

        for (LinkedOLE2Entry e : streams) {
            streamsListModel.addElement(e);
        }
    }

    private class WidowedStreams {
        private List<LinkedOLE2Entry> streams;

        public WidowedStreams(List<LinkedOLE2Entry> streams) {
            this.streams = streams;
        }

        public List<LinkedOLE2Entry> getStreams() {
            return streams;
        }

        @Override
        public String toString() {
            return "Widowed streams";
        }
    }

    private class SystemInformation {
        private final String type;
        private final int sid;

        public SystemInformation(String type, int sid) {
            this.type = type;
            this.sid = sid;
        }

        public int getSid() {
            return sid;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    private class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            Object nodeObject = ((DefaultMutableTreeNode) value).getUserObject();

            if (nodeObject instanceof FileListElement) {
                File file = ((FileListElement) nodeObject).getObject();
                ImageIcon icon = ImageLoader.loadByExtension(file.getName(), true);
                if (icon != null) {
                    label.setIcon(icon);
                }
            }

            if (nodeObject instanceof LinkedOLE2Entry) {
                ImageIcon icon = ImageLoader.load("folder.png");
                if (icon != null) {
                    label.setIcon(icon);
                }
            }

            if (nodeObject instanceof WidowedStreams) {
                ImageIcon icon = ImageLoader.load("widow_streams_folder.png");
                if (icon != null) {
                    label.setIcon(icon);
                }
            }

            if (nodeObject instanceof SystemInformation) {
                ImageIcon icon = ImageLoader.load("system.png");
                if (icon != null) {
                    label.setIcon(icon);
                }
            }

            return label;
        }
    }

    private class MyDefaultListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof LinkedOLE2Entry) {
                LinkedOLE2Entry entry = (LinkedOLE2Entry) value;
                if (entry.isStorage()) {
                    label.setIcon(ImageLoader.load("folder.png"));
                    label.setFont(new Font(label.getFont().getName(), Font.BOLD, label.getFont().getSize()));

                } else {
                    if (entry.getType().equals(OLE2Entry.EntryType.EMPTY) && !entry.isUnlincked()) {
                        label.setIcon(ImageLoader.load("removed_stream.png"));
                        label.setFont(new Font(label.getFont().getName(), Font.ITALIC, label.getFont().getSize()));
                    } else {
                        label.setIcon(ImageLoader.load("stream.png"));
                    }
                }
            }

            if (value instanceof SystemInformation) {
                ImageIcon icon = ImageLoader.load("system.png");
                if (icon != null) {
                    label.setIcon(icon);
                }
            }

            if (value instanceof SystemInformation) {
                //TODO
            }

            return label;
        }
    }

    private class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() instanceof JTree) {
                JTree jTree = (JTree) e.getSource();
                DefaultMutableTreeNode lastSelectedPathComponent = (DefaultMutableTreeNode) jTree.getLastSelectedPathComponent();
                if (lastSelectedPathComponent != null) {
                    Object userObject = lastSelectedPathComponent.getUserObject();
                    if (userObject instanceof LinkedOLE2Entry) {
                        LinkedOLE2Entry node = (LinkedOLE2Entry) userObject;
                        if (node != null) {
                            updateList(node);
                        }
                    }

                    if (userObject instanceof WidowedStreams) {
                        List<LinkedOLE2Entry> streams = ((WidowedStreams) userObject).getStreams();
                        updateList(streams);
                    }

                    if (userObject instanceof SystemInformation) {
                        updateList();
                    }
                }

                return;
            }

            if (e.getSource() instanceof JList && e.getClickCount() == 2) {
                JList list = (JList) e.getSource();
                Object selectedValue = list.getSelectedValue();

                if (selectedValue instanceof JListParentElement) {
                    selectedValue = ((JListParentElement) selectedValue).getObject();
                }

                if (selectedValue instanceof LinkedOLE2Entry) {
                    LinkedOLE2Entry node = (LinkedOLE2Entry) selectedValue;
                    if (node.isStorage()) {
                        updateList(node);
                    } else {
                        view.getController().setCurrentStream(node);
                    }
                }

                if (selectedValue instanceof SystemInformation) {
                    view.getController().setCurrentSector(((SystemInformation) selectedValue).getSid());
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
