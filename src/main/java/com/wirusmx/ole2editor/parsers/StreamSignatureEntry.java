package com.wirusmx.ole2editor.parsers;

public class StreamSignatureEntry {
    private int offset;
    private byte[] value;

    public StreamSignatureEntry(int offset, byte[] value) {
        this.offset = offset;
        this.value = value;
    }

    public int getOffset() {
        return offset;
    }

    public byte[] getValue() {
        return value;
    }
}
