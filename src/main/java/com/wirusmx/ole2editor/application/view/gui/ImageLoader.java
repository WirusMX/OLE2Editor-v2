package com.wirusmx.ole2editor.application.view.gui;

import javax.swing.*;
import java.io.File;

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
            extension = fileName.substring(pos + 1);
        }

        switch (extension) {
            case "doc":
                return load(PROGRAMS_ICONS_PATH + "msword.png");

            case "db":
                return load(PROGRAMS_ICONS_PATH + "db.png");

            case "xls":
                return load(PROGRAMS_ICONS_PATH + "msexcel.png");

            case "ppt":
                return load(PROGRAMS_ICONS_PATH + "mspowp.png");

        }

        if (useDefaultOle2Ico) {
            return load(PROGRAMS_ICONS_PATH + "other.png");
        }

        return load("file.png");
    }
}
