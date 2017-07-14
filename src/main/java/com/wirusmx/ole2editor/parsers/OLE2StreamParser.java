package com.wirusmx.ole2editor.parsers;

import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import java.util.List;

public abstract class OLE2StreamParser {

    public abstract List<Property> parse(LinkedOLE2Entry entry, byte[] streamBytes) throws IllegalFileStructure;

}
