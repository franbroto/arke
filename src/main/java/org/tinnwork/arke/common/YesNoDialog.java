package org.tinnwork.arke.common;

import java.io.Serializable;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;


public class YesNoDialog extends Window implements Button.ClickListener, Serializable {

	Callback callback;
    public Button yes = new Button("Si", this);
    Button no = new Button("No", this);

    public YesNoDialog(String caption, String question, Callback callback) {
        super(caption);

        VerticalLayout mainLayout = new VerticalLayout();
        setModal(true);
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        this.callback = callback;

        HorizontalLayout questions = new HorizontalLayout();
        questions.setMargin(true);
        questions.setSpacing(true);
        if (question != null) {
            Label labelQuestion = new Label(question);
            labelQuestion.addStyleName(ValoTheme.LABEL_LARGE);
            questions.addComponent(labelQuestion);
            mainLayout.addComponent(questions);
        }

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setMargin(true);
        buttons.setSpacing(true);
        yes.addStyleName(ValoTheme.BUTTON_PRIMARY);
        yes.setWidth(100,Unit.PIXELS);
        yes.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        no.setWidth(100,Unit.PIXELS);
        buttons.addComponent(yes);
        buttons.addComponent(no);
        mainLayout.addComponent(buttons);
        mainLayout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
        setContent(mainLayout);
    }

    public void buttonClick(Button.ClickEvent event) {
        if (getParent() != null) {
            this.close();
        }
        callback.onDialogResult(event.getSource() == yes);
    }

    public interface Callback {
        public void onDialogResult(boolean resultIsYes);
    }

}