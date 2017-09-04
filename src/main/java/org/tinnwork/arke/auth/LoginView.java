package org.tinnwork.arke.auth;

import java.io.Serializable;

import org.tinnwork.arke.MainUI;

import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;




//@SuppressWarnings("serial")
public class LoginView extends VerticalLayout implements Serializable {

    CheckBox rememberMe = new CheckBox("Recuérdame");

    public LoginView() {
        setSizeFull();
        setMargin(false);
        setSpacing(false);

        Component loginForm = buildLoginForm();
        addComponent(loginForm);
        setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    }

    private Component buildLoginForm() {
        final VerticalLayout loginPanel = new VerticalLayout();
        loginPanel.setSizeUndefined();
        loginPanel.setMargin(false);
        Responsive.makeResponsive(loginPanel);
        loginPanel.addStyleName("login-panel");

        loginPanel.addComponent(buildLogo());
        loginPanel.addComponent(buildFields());
        rememberMe.setValue(true);
        loginPanel.addComponent(rememberMe);
        return loginPanel;
    }

    private Component buildFields() {
        HorizontalLayout fields = new HorizontalLayout();
        fields.addStyleName("fields");

        final TextField username = new TextField("Usuario");
        username.setIcon(FontAwesome.USER);
        username.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final PasswordField password = new PasswordField("Contraseña");
        password.setIcon(FontAwesome.LOCK);
        password.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);

        final Button signin = new Button("Entrar", e -> onLogin(username.getValue(), password.getValue(), rememberMe.getValue()));
        signin.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        signin.addStyleName(ValoTheme.BUTTON_PRIMARY);
        signin.setClickShortcut(KeyCode.ENTER);
        signin.focus();

        fields.addComponents(username, password, signin);
        fields.setComponentAlignment(signin, Alignment.BOTTOM_LEFT);

        return fields;
    }

    private Component buildLogo() {
        CssLayout logoLayout = new CssLayout();
        Image logo = new Image(null, new ThemeResource("img/logo.png"));
        logo.setWidth(150,Unit.PIXELS);
        logoLayout.addComponent(logo);
        return logoLayout;
    }

    private void onLogin(String username, String password, boolean rememberMe) {
        if (AuthService.login(username, password, rememberMe)) {
            MainUI ui = (MainUI) UI.getCurrent();
            ui.showMainMenu();
        } else {
            Notification.show("Usuario o contraseña incorrectos", Notification.Type.ERROR_MESSAGE);
        }
    }

}
