package com.wirusmx.ole2editor.parsers.common;

import com.wirusmx.ole2editor.parsers.OLE2StreamParser;
import com.wirusmx.ole2editor.parsers.Property;
import com.wirusmx.ole2editor.parsers.StreamSignature;
import com.wirusmx.ole2editor.parsers.StreamSignatureEntry;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import java.util.List;

public class HeaderParser extends OLE2StreamParser {
    /**
     * Possible signatures of current stream
     */
    private static StreamSignature[] signatures = new StreamSignature[]{

    };

    public static StreamSignature[] getSignatures() {
        return signatures;
    }

    @Override
    public List<Property> parse(LinkedOLE2Entry entry, byte[] streamBytes) {
        return null;
    }
}
