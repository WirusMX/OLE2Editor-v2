package com.wirusmx.ole2editor.utils;

import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.io.OLE2Entry;
import com.wirusmx.ole2editor.io.OLE2InputStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Allows to manipulate with ole2 file inner structure
 *
 * @author WirusMX
 */
public class OLE2FileManager {
    private static List<OLE2Entry> getAllNodes(File currentFile) throws IOException, IllegalFileStructure {
        OLE2InputStream is = new OLE2InputStream(currentFile.getAbsolutePath());
        List<OLE2Entry> result = new ArrayList<>();

        while (is.hasNextEntry()) {
            result.add(is.readNextEntry());
        }

        return result;
    }

    public static String getNodesTreeAsString(File file) throws IOException, IllegalFileStructure {
        List<OLE2Entry> entries = getAllNodes(file);
        return file.getName() + buildTree(entries, 0, 1);
    }

    private static String buildTree(List<OLE2Entry> entries, int pos, int level) {
        if (pos >= entries.size()) {
            return "";
        }

        String t = "";
        for (int i = 0; i < level; i++) {
            t += "  ";
        }

        String result = "";
        if (entries.get(pos).getType() == OLE2Entry.EntryType.ROOT_STORAGE
                || entries.get(pos).getType() == OLE2Entry.EntryType.USER_STORAGE) {
            result += "\n" + t + "+ [" + entries.get(pos).getNameAsString().trim() + "]";
            result += buildTree(entries, entries.get(pos).getRootNodeID(), level + 1);
        } else {
            result += "\n" + t  + t + "[" + entries.get(pos).getNameAsString().trim() + "]";
        }

        if (entries.get(pos).getLeftChildID() >= 0) {
            result += buildTree(entries, entries.get(pos).getLeftChildID(), level);
        }

        if (entries.get(pos).getRightChildID() >= 0) {
            result += buildTree(entries, entries.get(pos).getRightChildID(), level);
        }

        return result;
    }
}
