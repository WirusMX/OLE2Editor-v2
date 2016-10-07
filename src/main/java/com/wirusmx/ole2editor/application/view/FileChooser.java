package com.wirusmx.ole2editor.application.view;

import java.io.File;

public interface FileChooser {
    int showOpenDialog();

    File getSelectedFile();
}
