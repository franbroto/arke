package org.tinnwork.arke.common;

import java.io.Serializable;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.auth.AuthService;

import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;



/**
 * Created by fran on 15/06/17.
 */
public class MainWindow extends VerticalLayout implements Serializable{


	public MainWindow() {
        setSizeFull();

        Component topBar = buildTopBar();
        addComponent(topBar);
        setComponentAlignment(topBar,Alignment.TOP_CENTER);
        setExpandRatio(topBar,1.0f);
    }

    private Component buildTopBar() {
        HorizontalLayout topBar = new HorizontalLayout();
        topBar.setSizeFull();

        Component leftItems = buildLeftItems();
        topBar.addComponent(leftItems);
        topBar.setComponentAlignment(leftItems,Alignment.TOP_LEFT);

        Component session = buildSession();
        topBar.addComponent(session);
        topBar.setComponentAlignment(session,Alignment.TOP_RIGHT);

        return topBar;
    }

    private Component buildSession() {
        HorizontalLayout session = new HorizontalLayout();

        String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
        final Label wellcome = new Label(username);
        wellcome.addStyleName(ValoTheme.LABEL_LARGE);

        final Button logOut = new Button("Cerrar sesión", this::onLogout);
        logOut.addStyleName(ValoTheme.BUTTON_SMALL);

        session.addComponents(wellcome,logOut);

        return session;
    }

    private Component buildLeftItems() {
        HorizontalLayout leftItems = new HorizontalLayout();
        Component mainButton = buildMainButton();
        Component backButton = buildBackButton();
        Component title = buildTitle();
        leftItems.addComponents(backButton,mainButton,title);

        return leftItems;
    }

    private Component buildTitle() {
        Label title = new Label();
        MainUI ui = (MainUI) UI.getCurrent();
        if (ui.currentScreen.equals(ui.SUBJECTS)){
            title.setValue("Asignaturas");
        } else if (ui.currentScreen.equals(ui.LESSONS)){
            title.setValue(ui.subject);
        } else if (ui.currentScreen.equals(ui.QUESTIONS)){
            title.setValue(ui.subject+"/"+ui.lesson);
        } else if (ui.currentScreen.equals(ui.GLOSSARY)){
            title.setValue("Glosario");
        }
        title.addStyleName(ValoTheme.LABEL_HUGE);
        //title.setWidth(200,Unit.PIXELS);
        title.setVisible(!ui.currentScreen.equals(ui.MAIN_MENU));
        return title;
    }

    private Component buildMainButton() {
        final Button mainButton = new Button("Menú", this::onBackToMenu);
        mainButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        mainButton.setIcon(new ThemeResource("img/logo-small.png"));
        mainButton.setWidth(85,Unit.PIXELS);
        MainUI ui = (MainUI) UI.getCurrent();
        mainButton.setVisible(!ui.currentScreen.equals(ui.MAIN_MENU));
        return mainButton;
    }

    private Component buildBackButton() {
        final Button backButton = new Button("Atrás", this::onUpLevel);
        backButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        backButton.setIcon(new ThemeResource("img/back.png"));
        backButton.setWidth(25,Unit.PIXELS);
        MainUI ui = (MainUI) UI.getCurrent();
        backButton.setVisible(!ui.currentScreen.equals(ui.MAIN_MENU)&&
                !ui.currentScreen.equals(ui.SUBJECTS)&&
                !ui.currentScreen.equals(ui.GLOSSARY));
        return backButton;
    }

    private void onLogout(Button.ClickEvent event) {
        AuthService.logOut();
    }

    private void onBackToMenu(Button.ClickEvent event){
        MainUI ui = (MainUI) UI.getCurrent();
        ui.showMainMenu();
    }

    private void onUpLevel(Button.ClickEvent event) {
        MainUI ui = (MainUI) UI.getCurrent();
        if (ui.currentScreen.equals(ui.LESSONS)) {
            ui.showSubjects();
        } else if (ui.currentScreen.equals(ui.QUESTIONS)) {
            ui.showLessons();
        }
    }
}