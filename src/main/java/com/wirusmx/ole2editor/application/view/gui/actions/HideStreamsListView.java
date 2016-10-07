package com.wirusmx.ole2editor.application.view.gui.actions;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HideStreamsListView implements ActionListener{
    private GuiView view;

    public HideStreamsListView(GuiView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!view.isStreamsListViewHide()){
            view.hideStreamsListView();
        } else {
            view.showStreamsListView();
        }
    }
}
