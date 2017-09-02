package org.tinnwork.arke.menu;

import java.io.Serializable;

import org.tinnwork.arke.common.MainWindow;

import com.vaadin.ui.Alignment;



public class MainMenuView extends MainWindow implements Serializable{

    public MainMenuView() {
        setSizeFull();

        MainMenuPanel mainMenuPanel = new MainMenuPanel();
        addComponent(mainMenuPanel);
        setComponentAlignment(mainMenuPanel,Alignment.TOP_CENTER);
        setExpandRatio(mainMenuPanel,10.0f);
    }
}
