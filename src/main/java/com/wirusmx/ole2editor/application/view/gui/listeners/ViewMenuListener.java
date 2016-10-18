package com.wirusmx.ole2editor.application.view.gui.listeners;

import com.wirusmx.ole2editor.application.view.gui.GuiView;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class ViewMenuListener implements MenuListener {
    private GuiView view;

    JCheckBoxMenuItem sectorsPanel;
    JCheckBoxMenuItem streamsPanel;
    JCheckBoxMenuItem systemInformationPanel;
    JCheckBoxMenuItem hexPanel;
    JCheckBoxMenuItem osfsPanel;
    JCheckBoxMenuItem streamPropertiesPanel;

    public ViewMenuListener(GuiView view, JCheckBoxMenuItem sectorsPanel, JCheckBoxMenuItem streamsPanel,
                            JCheckBoxMenuItem systemInformationPanel, JCheckBoxMenuItem hexPanel,
                            JCheckBoxMenuItem osfsPanel, JCheckBoxMenuItem streamPropertiesPanel) {
        this.view = view;
        this.sectorsPanel = sectorsPanel;
        this.streamsPanel = streamsPanel;
        this.systemInformationPanel = systemInformationPanel;
        this.hexPanel = hexPanel;
        this.osfsPanel = osfsPanel;
        this.streamPropertiesPanel = streamPropertiesPanel;
    }

    @Override
    public void menuSelected(MenuEvent e) {
        int code = view.getVisiblePanelsCode();

        sectorsPanel.setSelected((code & 1) == 1);
        streamsPanel.setSelected((code & 2) == 2);
        systemInformationPanel.setSelected((code & 4) == 4);
        hexPanel.setSelected((code & 8) == 8);
        osfsPanel.setSelected((code & 16) == 16);
        streamPropertiesPanel.setSelected((code & 32) == 32);
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
