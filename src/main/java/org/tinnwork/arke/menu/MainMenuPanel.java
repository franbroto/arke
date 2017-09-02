package org.tinnwork.arke.menu;

import java.io.Serializable;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.auth.AuthService;

import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;



public class MainMenuPanel extends Panel implements Serializable{

    MainMenuPanel() {
        super();
        String username = (String) VaadinSession.getCurrent().getAttribute(AuthService.SESSION_USERNAME);
        setCaption("Bienvenid@, " + username);
        setWidth("700px");
        setHeight("500px");

        VerticalLayout layout = new VerticalLayout();
        layout.setSizeFull();
        setStyleName("bordered");

        Component logo = buildLogo();
        layout.addComponent(logo);
        layout.setComponentAlignment(logo, Alignment.BOTTOM_CENTER);
        layout.setExpandRatio(logo, 3.0f);
        Component items = buildItems();
        layout.addComponent(items);
        layout.setComponentAlignment(items, Alignment.TOP_CENTER);
        layout.setExpandRatio(items, 4.0f);

        setContent(layout);
    }

    private Component buildLogo() {
        VerticalLayout logoLayout = new VerticalLayout();
        logoLayout.setSpacing(false);
        Image logo = new Image(null, new ThemeResource("img/logo.png"));
        logo.setWidth(200, Unit.PIXELS);
        Label name = new Label("Arke Quiz");
        name.setStyleName(ValoTheme.LABEL_NO_MARGIN);
        Label version = new Label("Version: 1.0");
        version.setStyleName(ValoTheme.LABEL_TINY);

        logoLayout.addComponents(logo, name, version);
        logoLayout.setComponentAlignment(logo, Alignment.TOP_CENTER);
        logoLayout.setComponentAlignment(name, Alignment.TOP_CENTER);
        logoLayout.setComponentAlignment(version, Alignment.TOP_CENTER);


        return logoLayout;
    }

    private Component buildItems() {
        VerticalLayout itemsLayout = new VerticalLayout();

        final Button questionsAndAnswers = new Button("Preguntas y respuestas", e -> goToQuestionsAndAsnwers());
        questionsAndAnswers.setWidth("100%");

        final Button glossary = new Button("Glosario", e -> goToGlossary());
        glossary.setWidth("100%");

        final Button groupPlay = new Button("Juego en grupo", e -> goToGroupPlay());
        groupPlay.setWidth("100%");

        //groupPlay.setEnabled(false);

        itemsLayout.addComponents(questionsAndAnswers, glossary, groupPlay);
        itemsLayout.setComponentAlignment(questionsAndAnswers, Alignment.TOP_CENTER);
        itemsLayout.setComponentAlignment(groupPlay, Alignment.TOP_CENTER);
        itemsLayout.setComponentAlignment(glossary, Alignment.TOP_CENTER);

        return itemsLayout;
    }

    private void goToQuestionsAndAsnwers() {
        MainUI ui = (MainUI) UI.getCurrent();
        ui.showSubjects();
    }

    private void goToGlossary() {
        MainUI ui = (MainUI) UI.getCurrent();
        ui.showGlossary();
    }

    private void goToGroupPlay() {
        MainUI ui = (MainUI) UI.getCurrent();
        ui.showGroupPlay();
    }
}