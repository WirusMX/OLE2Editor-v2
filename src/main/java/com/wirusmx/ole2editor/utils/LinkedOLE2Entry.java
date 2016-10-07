package com.wirusmx.ole2editor.utils;

import com.wirusmx.ole2editor.exceptions.IllegalMethodCallException;
import com.wirusmx.ole2editor.io.OLE2Entry;

import java.nio.ByteOrder;
import java.util.*;

public class LinkedOLE2Entry extends OLE2Entry {
    private final int fid;
    private LinkedOLE2Entry left = null;
    private LinkedOLE2Entry right = null;
    private LinkedOLE2Entry parent = null;
    private LinkedOLE2Entry child = null;
    private LinkedOLE2Entry prev = null;
    private LinkedOLE2Entry next = null;

    private LinkedOLE2Entry(OLE2Entry entry, int fid) {
        super(entry.getName(),
                entry.getNameLength(),
                entry.getType(),
                entry.getNodeColor(),
                entry.getLeftChildID(),
                entry.getRightChildID(),
                entry.getRootNodeID(),
                entry.getUniqueID(),
                entry.getUserFlags(),
                entry.getCreationTimeStamp(),
                entry.getModificationTimeStamp(),
                entry.getFirstStreamSectorID(),
                entry.getSize(),
                entry.getEndBytes(),
                entry.getByteOrder());

        this.fid = fid;
    }

    public int getFid() {
        return fid;
    }

    /**
     * @return left child of current entry in red-black tree if it exists,
     * <code>null</code> otherwise.
     */
    public LinkedOLE2Entry getLeft() {
        return left;
    }

    /**
     * @return right child of current entry in red-black tree if it exists,
     * <code>null</code> otherwise.
     */
    public LinkedOLE2Entry getRight() {
        return right;
    }

    /**
     * @return child entry if current stream is a storage,
     * <code>null</code> otherwise.
     */
    public LinkedOLE2Entry getChild() {
        return child;
    }

    /**
     * @return storage entry of current entry.
     * If current entry is root returns <code>null</code>.
     */
    public LinkedOLE2Entry getParent() {
        return parent;
    }

    /**
     * @return entry with file id = <code>current.fid - 1</code> if it exists,
     * <code>null</code> otherwise.
     */
    public LinkedOLE2Entry getPrev() {
        return prev;
    }

    /**
     * @return entry with file id = <code>current.fid + 1</code> if it exists,
     * <code>null</code> otherwise.
     */
    public LinkedOLE2Entry getNext() {
        return next;
    }

    /**
     * Inserts new entry into current storage with preserving red-black tree properties.
     *
     * @param name                - new entry name. Not more 31 characters.
     * @param type                - new entry type.
     * @param firstStreamSectorID - ID of the first sector of the entry stream.
     * @param size                - size of the stream.
     * @param byteOrder           - order of bytes.
     * @return file ID of the new entry.
     */
    @Deprecated
    public int addChild(String name, EntryType type, int firstStreamSectorID, int size, ByteOrder byteOrder) {
        LinkedOLE2Entry last = this;
        while (last.next != null) {
            last = last.next;
        }

        LinkedOLE2Entry current = new LinkedOLE2Entry(
                new OLE2Entry(
                        new byte[64],
                        64,
                        type,
                        EntryColor.RED,
                        -1,
                        -1,
                        -1,
                        new byte[16],
                        new byte[4],
                        new Date(),
                        new Date(),
                        firstStreamSectorID,
                        size,
                        new byte[4],
                        byteOrder
                ),

                last.getFid() + 1
        );

        current.parent = this;
        current.prev = last;

        return current.getFid();
    }

    @Deprecated
    public void delete() {

    }

    @Deprecated
    public void rename() {

    }

    @Deprecated
    public void move(LinkedOLE2Entry newStorage) {

    }

    @Deprecated
    public void copy(LinkedOLE2Entry storage) {

    }

    /**
     * Converts current <code>{@link LinkedOLE2Entry}</code> element
     * to <code>{@link OLE2Entry}</code>.
     *
     * @return resulted <code>{@link OLE2Entry}</code>.
     */
    public OLE2Entry toOLE2Entry() {
        return this;
    }

    public String getAbsolutePath(){
        String path = getNameAsString();
        LinkedOLE2Entry t = getParent();
        while (t != null){
            path = t.getNameAsString() + "/" + path;
            t = t.getParent();
        }

        return path;
    }

    /**
     * @return list of <code>{@link LinkedOLE2Entry}</code>, which contains in current storage,
     * order by type and name.
     * @throws IllegalMethodCallException if current entry is not a storage.
     */
    public List<LinkedOLE2Entry> entriesList() {
        if (child == null) {
            throw new IllegalMethodCallException("Stream with type:" + getType().toString() + " not contains children");
        }

        List<LinkedOLE2Entry> result = new ArrayList<>();
        addChildren(child, result);
        Collections.sort(result, new Comparator<OLE2Entry>() {
            @Override
            public int compare(OLE2Entry o1, OLE2Entry o2) {
                if (o1 == null) {
                    return 1;
                }

                if (o2 == null) {
                    return -1;
                }

                if (o1.getType().equals(OLE2Entry.EntryType.USER_STORAGE)) {
                    if (!o2.getType().equals(OLE2Entry.EntryType.USER_STORAGE)) {
                        return -1;
                    }
                }

                if (!o1.getType().equals(OLE2Entry.EntryType.USER_STORAGE)) {
                    if (o2.getType().equals(OLE2Entry.EntryType.USER_STORAGE)) {
                        return 1;
                    }
                }

                return o1.getNameAsString().toLowerCase().compareTo(o2.getNameAsString().toLowerCase());

            }
        });

        return result;
    }

    /**
     * Adds children of current entry in red-black tree into the list.
     *
     * @param current - current entry.
     * @param entries - target list.
     */
    private void addChildren(LinkedOLE2Entry current, List<LinkedOLE2Entry> entries) {
        if (current == null) {
            return;
        }

        entries.add(current);
        addChildren(current.left, entries);
        addChildren(current.right, entries);
    }

    /**
     * Transforms red-black tree represented as array of <code>{@link OLE2Entry}</code>
     * to red-black tree based on <code>{@link LinkedOLE2Entry}</code>.
     *
     * @param entries - list of red-black tree entries, ordered by fid.
     *                Must contains at list one element.
     * @return root element of red-black tree built on <code>entries</code>.
     * @throws IllegalArgumentException if entries list is empty
     *                                  or list contains entry (entries) unlinked with
     *                                  other entries.
     */
    public static LinkedOLE2Entry buildByEntriesList(List<OLE2Entry> entries) {
        if (entries == null || entries.size() == 0) {
            throw new IllegalArgumentException("Entries array must contains at list one element");
        }

        LinkedOLE2Entry[] linkedOLE2Entries = new LinkedOLE2Entry[entries.size()];
        boolean[] linked = new boolean[entries.size()];

        for (int i = 0; i < linkedOLE2Entries.length; i++) {
            linkedOLE2Entries[i] = new LinkedOLE2Entry(entries.get(i), i);
        }

        for (int i = 0; i < linkedOLE2Entries.length; i++) {
            if (entries.get(i).getType().equals(EntryType.EMPTY)){
                linked[i] = true;
                continue;
            }

            if (i > 0) {
                linkedOLE2Entries[i].prev = linkedOLE2Entries[i - 1];
            }

            if (i + 1 < linkedOLE2Entries.length) {
                linkedOLE2Entries[i].next = linkedOLE2Entries[i + 1];
            }

            int id = entries.get(i).getLeftChildID();
            if (id >= 0 && id < entries.size()) {
                linkedOLE2Entries[i].left = linkedOLE2Entries[id];
                linked[id] = true;
            }

            id = entries.get(i).getRightChildID();
            if (id >= 0 && id < entries.size()) {
                linkedOLE2Entries[i].right = linkedOLE2Entries[id];
                linked[id] = true;
            }

            id = entries.get(i).getRootNodeID();
            if (id >= 0 && id < entries.size()) {
                linkedOLE2Entries[i].child = linkedOLE2Entries[id];
                linked[id] = true;
            }
        }

        for (int i = 1; i < linked.length; i++) {
            if (!linked[i]) {
                throw new IllegalArgumentException("Red-black tree cat not contains unlinked elements.");
            }
        }

        addParent(linkedOLE2Entries[0], null);

        return linkedOLE2Entries[0];
    }

    /**
     * Links current entry with its storage entry.
     *
     * @param current - current entry.
     * @param parent  - storage entry.
     */
    private static void addParent(LinkedOLE2Entry current, LinkedOLE2Entry parent) {
        current.parent = parent;
        if (current.left != null) {
            addParent(current.left, parent);
        }

        if (current.right != null) {
            addParent(current.right, parent);
        }

        if (current.child != null) {
            addParent(current.child, current);
        }
    }
}
