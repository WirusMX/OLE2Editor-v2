package com.wirusmx.ole2editor.model;

import com.wirusmx.ole2editor.controller.Controller;
import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.utils.OLE2FileManager;

import java.io.IOException;

public class Model {

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public String getStreamsTree() throws IOException, IllegalFileStructure {
        return OLE2FileManager.getNodesTreeAsString(controller.getCurrentFile());
    }
}
