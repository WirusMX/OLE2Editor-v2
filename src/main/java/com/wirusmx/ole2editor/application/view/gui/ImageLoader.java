package com.wirusmx.ole2editor.application.view.gui;

import javax.swing.*;
import java.io.File;

public class ImageLoader {
    private static final String IMAGES_PATH = "img/";

    public static ImageIcon load (String name){
        String imagePath = IMAGES_PATH + name;
        if (new File(imagePath).exists()){
            return new ImageIcon(imagePath);
        }

        throw new IllegalArgumentException("File " + imagePath + " not found!");
    }
}
