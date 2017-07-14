package com.wirusmx.ole2editor.parsers.common;

import com.wirusmx.ole2editor.exceptions.IllegalFileStructure;
import com.wirusmx.ole2editor.parsers.OLE2StreamParser;
import com.wirusmx.ole2editor.parsers.Property;
import com.wirusmx.ole2editor.parsers.StreamSignature;
import com.wirusmx.ole2editor.parsers.StreamSignatureEntry;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.wirusmx.ole2editor.parsers.Property.ValueType.TEXT;
import static com.wirusmx.ole2editor.utils.Converter.bytesToInt16;

public class SummaryInformationStreamParser extends OLE2StreamParser {
    /**
     * Possible signatures of current stream
     */
    private static StreamSignature[] signatures = new StreamSignature[]{
            new StreamSignature(new StreamSignatureEntry[]{
                    new StreamSignatureEntry(28,
                            new byte[]{(byte) 0xE0, (byte) 0x85, (byte) 0x9F, (byte) 0xF2, (byte) 0xF9, 0x4F, 0x68, 0x10,
                                    (byte) 0xAB, (byte) 0x91, 0x08, 0x00, 0x2B, 0x27, (byte) 0xB3, (byte) 0xD9})
            })
    };

    public static StreamSignature[] getSignatures() {
        return signatures;
    }

    @Override
    public List<Property> parse(LinkedOLE2Entry entry, byte[] streamBytes) throws IllegalFileStructure {
        List<Property> properties = new ArrayList<>();
        ByteOrder byteOrder;
        int t = bytesToInt16(ByteOrder.LITTLE_ENDIAN, streamBytes[0], streamBytes[1]);
        switch (t) {
            case -1: {
                byteOrder = ByteOrder.BIG_ENDIAN;
                properties.add(new Property(0, Arrays.copyOfRange(streamBytes, 0, 2), "Big endian", TEXT, "Byte order"));
                break;
            }
            case -2: {
                byteOrder = ByteOrder.LITTLE_ENDIAN;
                properties.add(new Property(0, Arrays.copyOfRange(streamBytes, 0, 2), "Little endian", TEXT, "Byte order"));
                break;
            }
            default: {
                throw new IllegalFileStructure("Unsupported byte order in stream " + entry.getNameAsString());
            }
        }



        return properties;
    }
}
