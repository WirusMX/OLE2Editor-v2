package com.wirusmx.ole2editor.parsers;

import com.wirusmx.ole2editor.parsers.common.SummaryInformationStreamParser;
import com.wirusmx.ole2editor.parsers.thumbsdb.ThumbsdbImageStreamParser;
import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

import java.util.HashMap;
import java.util.Map;

public class OLE2StreamParsersFactory {
    private static OLE2StreamParsersFactory instance;

    private Map<StreamSignature, Class> parsers;

    private OLE2StreamParsersFactory() {
        loadParsers();
    }

    public static OLE2StreamParsersFactory getInstance() {
        if (instance == null) {
            instance = new OLE2StreamParsersFactory();
        }

        return instance;
    }

    public OLE2StreamParser getOle2StreamParser(byte[] streamBytes) throws IllegalAccessException, InstantiationException {
        for (StreamSignature s: parsers.keySet()){
            if (s.matchesTo(streamBytes)){
                return (OLE2StreamParser) parsers.get(s).newInstance();
            }
        }

        return new DefaultParser();
    }

    private void loadParsers(){
        parsers = new HashMap<>();

        parsers.put(ThumbsdbImageStreamParser.getSignatures()[0], ThumbsdbImageStreamParser.class);
        parsers.put(ThumbsdbImageStreamParser.getSignatures()[1], ThumbsdbImageStreamParser.class);
        parsers.put(SummaryInformationStreamParser.getSignatures()[0], SummaryInformationStreamParser.class);
    }
}
