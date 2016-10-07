package com.wirusmx.ole2editor.application.view.gui.wrappers;


public class JListParentElement<T> implements JListElementWrapper<T> {
    private T object;

    public JListParentElement(T object) {
        this.object = object;
    }

    @Override
    public T getObject() {
        return object;
    }

    @Override
    public String toString() {
        return "[..]";
    }
}
