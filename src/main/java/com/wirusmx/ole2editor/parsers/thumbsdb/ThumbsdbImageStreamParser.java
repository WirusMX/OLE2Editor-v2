package com.wirusmx.ole2editor.parsers.thumbsdb;

import com.wirusmx.ole2editor.parsers.OLE2StreamParser;
import com.wirusmx.ole2editor.parsers.Property;
import com.wirusmx.ole2editor.parsers.StreamSignature;
import com.wirusmx.ole2editor.parsers.StreamSignatureEntry;
import com.wirusmx.ole2editor.utils.Converter;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.wirusmx.ole2editor.parsers.Property.ValueType.BYTES;
import static com.wirusmx.ole2editor.parsers.Property.ValueType.IMAGE;
import static com.wirusmx.ole2editor.parsers.Property.ValueType.TEXT;

/**
 * Parser is used for streams, which contains image thumbnail, in Thumbs.db file.
 * Such streams have one of next structures:
 * 1. Structure, which characteristic for Thumbs.db, which is created by Windows XP OS
 * or previous:
 * (0000) 0x0000, 4, header length
 * (0004) 0x0004, 4, ? 0x00000001 - may be count of sections before data
 * (0008) 0x0008, 4, data(image) length *
 * (0012) 0x000C, *, data(image)
 * Such streams have numeric name, for example "1". This name is a mirrored number of
 * thumbnail in Thumbs.db file.
 * Thumbnail in such stream has size 90 pix on greater side.
 * <p>
 * 2. Structure, which characteristic for Thumbs.db, which is created by Windows 7:
 * (0000) 0x0000, 4, header length
 * (0004) 0x0004, 4, ? 0x00000003 - may be count of sections before data
 * (0008) 0x0008, 4, data(image) length *
 * (0012) 0x000C, 4, ? 0x00000000
 * (0016) 0x0010, 8, ?
 * (0024) 0x0018, *, data(image)
 * Such streams have name like "256_96b63c544e7374d0".
 * Thumbnail in such stream has size 256 pix on greater side.
 */
public class ThumbsdbImageStreamParser extends OLE2StreamParser {
    /**
     * Possible signatures of current stream
     */
    private static StreamSignature[] signatures = new StreamSignature[]{
            new StreamSignature(new StreamSignatureEntry[]{
                    new StreamSignatureEntry(0, new byte[]{0x0C, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00})
            }),
            new StreamSignature(new StreamSignatureEntry[]{
                    new StreamSignatureEntry(0, new byte[]{0x18, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00}),
                    new StreamSignatureEntry(12, new byte[]{0x00, 0x00, 0x00, 0x00})
            })
    };

    public static StreamSignature[] getSignatures() {
        return signatures;
    }

    @Override
    public List<Property> parse(LinkedOLE2Entry entry, byte[] streamBytes) {
        List<Property> properties = new ArrayList<>();
        int headerLength = Converter.bytesToInt32(entry.getByteOrder(), Arrays.copyOfRange(streamBytes, 0, 4));
        int sectionsCount = Converter.bytesToInt32(entry.getByteOrder(), Arrays.copyOfRange(streamBytes, 4, 8));
        int imageDataLength = Converter.bytesToInt32(entry.getByteOrder(), Arrays.copyOfRange(streamBytes, 8, 12));
        properties.add(new Property(0, Arrays.copyOfRange(streamBytes, 0, 4), headerLength, TEXT, "Header length"));
        properties.add(new Property(4, Arrays.copyOfRange(streamBytes, 4, 8), sectionsCount, TEXT, "Sections count"));
        properties.add(new Property(8, Arrays.copyOfRange(streamBytes, 8, 12), imageDataLength, TEXT, "Image data length"));
        if (sectionsCount == 3){
            properties.add(new Property(12, Arrays.copyOfRange(streamBytes, 12, 16), "Unknown"));
            properties.add(new Property(16, Arrays.copyOfRange(streamBytes, 16, 24), "Unknown. Possible checksum"));
        }
        properties.add(new Property(headerLength, Arrays.copyOfRange(streamBytes, headerLength, streamBytes.length),
                new ImageIcon(Arrays.copyOfRange(streamBytes, headerLength, streamBytes.length)), IMAGE, "Image"));

        return properties;
    }

}
