package com.wirusmx.ole2editor.parsers;

public class Property {
    private int offset;
    private int length;
    private Object value;
    private ValueType type;
    private String description;
    private boolean isUnknown;
    private byte[] bytes;


    public Property(int offset, byte[] bytes, Object value, ValueType type, String description) {
        this.offset = offset;
        this.length = bytes.length;
        this.bytes = bytes;
        this.value = value;
        this.type = type;
        this.description = description;
        this.isUnknown = false;
    }

    public Property(int offset, byte[] bytes, String description) {
        this.offset = offset;
        this.length = bytes.length;
        this.bytes = bytes;
        this.value = null;
        this.type = ValueType.BYTES;
        this.description = description;
        this.isUnknown = true;
    }

    public int getOffset() {
        return offset;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getLength() {
        return length;
    }

    public Object getValue() {
        return value;
    }

    public ValueType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUnknown() {
        return isUnknown;
    }

    public enum ValueType {
        TEXT,
        IMAGE,
        BYTES
    }

}
