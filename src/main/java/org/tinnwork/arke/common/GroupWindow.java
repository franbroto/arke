package org.tinnwork.arke.common;

import com.vaadin.server.Sizeable;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.io.Serializable;

import org.tinnwork.arke.MainUI;

/**
 * Created by fran on 12/08/17.
 */
public class GroupWindow extends VerticalLayout implements Serializable {

	public GroupWindow() {
        setSizeFull();
        addStyleName("backColorBlue");

        Component topBar = buildTopBar();
        addComponent(topBar);
        setComponentAlignment(topBar, Alignment.TOP_CENTER);
        setExpandRatio(topBar,1.0f);
    }

    private Component buildTopBar() {
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setSizeFull();

        Component leftItems = buildLeftItems();
        topBar.addComponent(leftItems);
        topBar.setComponentAlignment(leftItems,Alignment.TOP_LEFT);

        return topBar;
    }

    private Component buildLeftItems() {
        HorizontalLayout leftItems = new HorizontalLayout();
        Component mainButton = buildMainButton();
        leftItems.addComponents(mainButton);

        return leftItems;
    }


    private Component buildMainButton() {
        final Button mainButton = new Button("", this::onBackToMenu);
        mainButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        mainButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        mainButton.setIcon(new ThemeResource("img/logo-small-blue.png"));
        mainButton.setWidth(85, Sizeable.Unit.PIXELS);
        MainUI ui = (MainUI) UI.getCurrent();
        mainButton.setVisible(!ui.currentScreen.equals(ui.MAIN_MENU));
        return mainButton;
    }


    private void onBackToMenu(Button.ClickEvent event){
        MainUI ui = (MainUI) UI.getCurrent();
        ui.showMainMenu();
    }

}
