package com.wirusmx.ole2editor.application.view.gui.wrappers;

import com.wirusmx.ole2editor.utils.LinkedOLE2Entry;

public class StreamsListElement implements JListElementWrapper<LinkedOLE2Entry>{
    private LinkedOLE2Entry object;

    public StreamsListElement(LinkedOLE2Entry object) {
        this.object = object;
    }

    @Override
    public LinkedOLE2Entry getObject() {
        return object;
    }

    @Override
    public String toString() {
        return object.toString();
    }
}
