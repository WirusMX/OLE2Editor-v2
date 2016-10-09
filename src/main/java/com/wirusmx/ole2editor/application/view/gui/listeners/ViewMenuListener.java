package com.wirusmx.ole2editor.application.view.gui.listeners;

import com.wirusmx.ole2editor.application.view.View;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class ViewMenuListener implements MenuListener {
    private View view;

    JCheckBoxMenuItem sectorsPanel;
    JCheckBoxMenuItem streamsPanel;
    JCheckBoxMenuItem systemInformationPanel;
    JCheckBoxMenuItem hexPanel;
    JCheckBoxMenuItem osfsPanel;
    JCheckBoxMenuItem streamPropertiesPanel;

    public ViewMenuListener(View view, JCheckBoxMenuItem sectorsPanel, JCheckBoxMenuItem streamsPanel,
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
        int code = view.getVisiblePanels();

        if ((code & 1) == 1){
            sectorsPanel.setSelected(true);
        }

        if ((code & 2) == 2){
            streamsPanel.setSelected(true);
        }

        if ((code & 4) == 4){
            systemInformationPanel.setSelected(true);
        }

        if ((code & 8) == 8){
            hexPanel.setSelected(true);
        }

        if ((code & 16) == 16){
            osfsPanel.setSelected(true);
        }

        if ((code & 32) == 32){
            streamPropertiesPanel.setSelected(true);
        }
    }

    @Override
    public void menuDeselected(MenuEvent e) {

    }

    @Override
    public void menuCanceled(MenuEvent e) {

    }
}
