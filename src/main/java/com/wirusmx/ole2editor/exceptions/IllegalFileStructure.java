package com.wirusmx.ole2editor.exceptions;

/**
 * Exceptions throws when file has not ole2 structure
 */
public class IllegalFileStructure extends Exception {
    private String message;

    public IllegalFileStructure() {
    }

    public IllegalFileStructure(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        if (message.length() != 0){
            return message;
        }
        return "File has illegal structure";
    }

    @Override
    public String getLocalizedMessage() {
        return getMessage();
    }
}
