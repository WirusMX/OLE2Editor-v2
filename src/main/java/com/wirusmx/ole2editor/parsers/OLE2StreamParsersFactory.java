package com.wirusmx.ole2editor.parsers;

import java.util.HashMap;
import java.util.Map;

public class OLE2StreamParsersFactory {
    private static OLE2StreamParsersFactory instance;

    private Map<byte[], OLE2StreamParser> parsers;

    private OLE2StreamParsersFactory() {

    }

    public static OLE2StreamParsersFactory getInstance() {
        if (instance == null) {
            instance = new OLE2StreamParsersFactory();
        }

        return instance;
    }

    public OLE2StreamParser getOle2StreamParser(byte[] streamSignature) {
        if (parsers.containsKey(streamSignature)){
            return parsers.get(streamSignature);
        }

        return new DefaultParser();
    }

    private void loadParsers(){
        parsers = new HashMap<>();
    }
}
