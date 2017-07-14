package com.wirusmx.ole2editor.parsers;

import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import java.util.ArrayList;
import java.util.List;

import static com.wirusmx.ole2editor.parsers.Property.ValueType.BYTES;

/**
 * Default OLE2 stream parser. It is used for all streams, for which parsers
 * not implemented yet.
 */
public class DefaultParser extends OLE2StreamParser {
    /**
     * Always returns {@link java.util.List} of one {@link com.wirusmx.ole2editor.parsers.Property},
     * which does not carry a payload.
     *
     * @param streamBytes - bytes of stream for parsing
     * @return {@link java.util.List} of one {@link com.wirusmx.ole2editor.parsers.Property},
     * which always contains such values:
     * <code>offset = 0</code>;
     * <code>length = streamBytes.length</code>;
     * <code>description = "Bytes"</code>.
     */
    @Override
    public List<Property> parse(LinkedOLE2Entry entry, byte[] streamBytes) {
        List<Property> properties = new ArrayList<>();
        properties.add(new Property(0, streamBytes, "Bytes of '" + entry.getNameAsString() + "'"));
        return properties;
    }
}
