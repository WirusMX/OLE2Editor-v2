package com.wirusmx.ole2editor.exceptions;

/**
 * Exceptions throws when file has not ole2 structure
 */
public class IllegalFileStructure extends Exception {
    @Override
    public String getMessage() {
        return "File has illegal structure";
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
}
