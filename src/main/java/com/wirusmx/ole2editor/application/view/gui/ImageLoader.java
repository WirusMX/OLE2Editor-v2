package com.wirusmx.ole2editor.application.view.gui;

import javax.swing.*;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ImageLoader {
    private static final String IMAGES_PATH = "img/";
    private static final String PROGRAMS_ICONS_PATH = "programs/";


    public static ImageIcon load(String name) {
        String imagePath = IMAGES_PATH + name;
        if (new File(imagePath).exists()) {
            return new ImageIcon(imagePath);
        }

        return null;
    }

    public static ImageIcon loadByExtension(String fileName, boolean useDefaultOle2Ico) {
        String extension = "";
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos + 1 < fileName.length()) {
            extension = fileName.substring(pos + 1).toLowerCase();
        }

        String propPath = IMAGES_PATH + "/" + PROGRAMS_ICONS_PATH + "/" + "ext.properties";
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader(propPath)) {
            properties.load(fileReader);
            if (properties.containsKey(extension)){
                return load(PROGRAMS_ICONS_PATH + properties.getProperty(extension));
            }
        } catch (IOException ignored) {
        }

        if (useDefaultOle2Ico) {
            return load(PROGRAMS_ICONS_PATH + "other.png");
        }

        return load("file.png");
    }
}
