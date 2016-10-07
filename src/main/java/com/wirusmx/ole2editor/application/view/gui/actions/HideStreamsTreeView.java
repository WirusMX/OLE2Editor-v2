package com.wirusmx.ole2editor.application.view.gui.actions;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HideStreamsTreeView implements ActionListener{
    private GuiView view;

    public HideStreamsTreeView(GuiView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!view.isStreamsTreeViewHide()){
            view.hideStreamsTreeView();
        } else {
            view.showStreamsTreeView();
        }
    }
}
