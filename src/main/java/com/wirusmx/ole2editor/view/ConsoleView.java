package com.wirusmx.ole2editor.view;

import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.utils.OLE2FileManager;

import java.io.File;
import java.io.IOException;

public class ConsoleView extends GUIView {
    private static final String FILE_NAME = "/home/wirusmx/1.doc";
    public void init() {
        try {
            System.out.println(OLE2FileManager.getNodesTreeAsString(new File(FILE_NAME)));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalFileStructure illegalFileStructure) {
            illegalFileStructure.printStackTrace();
        }
    }
}
