package com.wirusmx.ole2editor.parsers;

import java.util.List;

public abstract class OLE2StreamParser {
    public abstract List<Property> parse(byte[] streamBytes);
}
