package org.tinnwork.arke.questions;

import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import   com.vaadin.ui.CheckBox;
import java.sql.SQLException;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.common.YesNoDialog;

import static com.vaadin.ui.UI.getCurrent;

/**
 * Created by fran on 16/06/17.
 */
public class QuestionForm extends FormLayout {

    Button save = new Button("Guardar", this::save);
    private Button cancel = new Button("Cancelar", this::cancel);
    Button delete = new Button("Borrar", this::delete);
    private TextField question = new TextField("Pregunta");
    private CheckBox vPIR = new CheckBox("PIR");
    private TextField answer1 = new TextField("Respuesta 1");
    private TextField answer2 = new TextField("Respuesta 2");
    private TextField answer3 = new TextField("Respuesta 3");
    private TextField answer4 = new TextField("Respuesta 4");
    private CheckBox vAnswer1 = new CheckBox();
    private CheckBox vAnswer2 = new CheckBox();
    private CheckBox vAnswer3 = new CheckBox();
    private CheckBox vAnswer4 = new CheckBox();
    private TextField exposition = new TextField("Explicación");
    private Label newOrEditTitle = new Label("Añadir pregunta");
    public Question selectedItem;

    public QuestionForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        setVisible(false);
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);
        this.addStyleName("v-scrollable");
        this.setHeight("100%");

        newOrEditTitle.addStyleName(ValoTheme.LABEL_H2);
        question.setWidth(400,Unit.PIXELS);
        answer1.setWidth(400,Unit.PIXELS);
        answer2.setWidth(400,Unit.PIXELS);
        answer3.setWidth(400,Unit.PIXELS);
        answer4.setWidth(400,Unit.PIXELS);
        exposition.setWidth(400,Unit.PIXELS);
        HorizontalLayout questionWithPIR = new HorizontalLayout();
        questionWithPIR.addComponents(question,vPIR);
        questionWithPIR.setComponentAlignment(vPIR,Alignment.TOP_RIGHT);
        questionWithPIR.setWidth(400,Unit.PIXELS);
        HorizontalLayout expositionHL = new HorizontalLayout();
        expositionHL.addComponents(exposition);
        expositionHL.setWidth(400,Unit.PIXELS);
        HorizontalLayout answer1WithCB = new HorizontalLayout();
        answer1WithCB.addComponents(answer1,vAnswer1);
        answer1WithCB.setComponentAlignment(vAnswer1,Alignment.TOP_RIGHT);
        answer1WithCB.setWidth(400,Unit.PIXELS);
        HorizontalLayout answer2WithCB = new HorizontalLayout();
        answer2WithCB.addComponents(answer2,vAnswer2);
        answer2WithCB.setComponentAlignment(vAnswer2,Alignment.TOP_RIGHT);
        answer2WithCB.setWidth(400,Unit.PIXELS);
        HorizontalLayout answer3WithCB = new HorizontalLayout();
        answer3WithCB.addComponents(answer3,vAnswer3);
        answer3WithCB.setComponentAlignment(vAnswer3,Alignment.TOP_RIGHT);
        answer3WithCB.setWidth(400,Unit.PIXELS);
        HorizontalLayout answer4WithCB = new HorizontalLayout();
        answer4WithCB.addComponents(answer4,vAnswer4);
        answer4WithCB.setComponentAlignment(vAnswer4,Alignment.TOP_RIGHT);
        answer4WithCB.setWidth(400,Unit.PIXELS);
        save.setWidth(400,Unit.PIXELS);
        cancel.setWidth(400,Unit.PIXELS);
        delete.setWidth(400,Unit.PIXELS);
        delete.setVisible(false);

        addComponents(newOrEditTitle,questionWithPIR,expositionHL,answer1WithCB,answer2WithCB,answer3WithCB,answer4WithCB,save,cancel,delete);
    }

    void newQuestion() {
        selectedItem=null;

        newOrEditTitle.setValue("Añadir pregunta");
        question.setValue("");
        vPIR.setValue(false);
        answer1.setValue("");
        answer2.setValue("");
        answer3.setValue("");
        answer4.setValue("");
        vAnswer1.setValue(false);
        vAnswer2.setValue(false);
        vAnswer3.setValue(false);
        vAnswer4.setValue(false);
        exposition.setValue("");

        delete.setVisible(false);
        setVisible(true);
    }

    void edit(Question entry) {
        selectedItem=entry;
        newOrEditTitle.setValue("Editar pregunta");
        question.setValue(entry.getQuestion());
        vPIR.setValue(entry.getVPir());
        answer1.setValue(entry.getAnswer1());
        answer2.setValue(entry.getAnswer2());
        answer3.setValue(entry.getAnswer3());
        answer4.setValue(entry.getAnswer4());
        vAnswer1.setValue(entry.getVAnswer1());
        vAnswer2.setValue(entry.getVAnswer2());
        vAnswer3.setValue(entry.getVAnswer3());
        vAnswer4.setValue(entry.getVAnswer4());
        exposition.setValue(entry.getExposition());

        delete.setVisible(true);
        setVisible(entry != null);
    }

    public void save(Button.ClickEvent event) {
        MainUI ui = (MainUI) getCurrent();
        if (selectedItem == null) {
            if (question != null && !question.getValue().equals("")) {
                if ((answer1 != null && !answer1.getValue().equals("")) &&
                        (answer2 != null && !answer2.getValue().equals("")) &&
                        (answer3 != null && !answer3.getValue().equals(""))) {
                    if  (vAnswer1.getValue() ||
                            vAnswer2.getValue() ||
                            vAnswer3.getValue() ||
                            (!answer4.getValue().equals("")&&vAnswer4.getValue())) {

                        if (ui.id_lesson==null) {
                            if (ui.lessonService.findAll("tema=" + ui.lesson).size() > 0) {
                                int index=ui.lessonService.findAll("tema=" + ui.lesson).size()-1;
                                ui.id_lesson = ui.lessonService.findAll("tema=" + ui.lesson).get(index).getId();
                            }
                        }

                        Question newQuestion = new Question(null, question.getValue(),
                                answer1.getValue(), answer2.getValue(), answer3.getValue(), answer4.getValue(),
                                vAnswer1.getValue(), vAnswer2.getValue(), vAnswer3.getValue(), vAnswer4.getValue(),
                                vPIR.getValue(), exposition.getValue(),
                                ui.id_lesson);
                         try {
                             ui.questionService.save(newQuestion);
                             Notification notification = new Notification("Pregunta "+ getShortenedQuestion(question.getValue())+" añadida");
                             notification.show(Page.getCurrent());
                             setVisible(false);
                         } catch (SQLException e) {}
                    } else {
                        Notification notification = new Notification("Elige al menos una respuesta correcta");
                        notification.show(Page.getCurrent());
                    }
                } else {
                    Notification notification = new Notification("Rellena al menos tres respuestas respuestas");
                    notification.show(Page.getCurrent());
                }
            } else {
                Notification notification = new Notification("Rellena el campo \"Pregunta\"");
                notification.show(Page.getCurrent());
            }
        } else if (question != null && !question.getValue().equals("")) {
            if ((answer1 != null && !answer1.getValue().equals("")) &&
                    (answer2 != null && !answer2.getValue().equals("")) &&
                    (answer3 != null && !answer3.getValue().equals(""))) {
                if (vAnswer1.getValue() ||
                        vAnswer2.getValue() ||
                        vAnswer3.getValue() ||
                        (!answer4.getValue().equals("")&&vAnswer4.getValue())) {
                    try {
                        ui.questionService.edit(new Question(selectedItem.getId(), question.getValue(),
                                answer1.getValue(), answer2.getValue(), answer3.getValue(), answer4.getValue(),
                                vAnswer1.getValue(), vAnswer2.getValue(), vAnswer3.getValue(), vAnswer4.getValue(),
                                vPIR.getValue(), exposition.getValue(),
                                selectedItem.getIdLesson()));
                        Notification notification = new Notification("Pregunta " + getShortenedQuestion(question.getValue()) + " guardada");
                        notification.show(Page.getCurrent());
                    }catch (SQLException e){}
                } else {
                    Notification notification = new Notification("Elige al menos una respuesta correcta");
                    notification.show(Page.getCurrent());
                }
            } else {
                Notification notification = new Notification("Rellena al menos tres respuestas");
                notification.show(Page.getCurrent());
            }
        } else {
            Notification notification = new Notification("Rellena el campo \"Pregunta\"");
            notification.show(Page.getCurrent());
        }
    }

    void cancel(Button.ClickEvent event) {
        setVisible(false);
    }

    void delete(Button.ClickEvent event) {
        MainUI ui= (MainUI) getCurrent();
        ui.addWindow( new YesNoDialog(
                "Borrar pregunta",
                "¿Está seguro de que desea eliminar permanentemente la pregunta " + getShortenedQuestion(selectedItem.getQuestion()),
                new YesNoDialog.Callback() {
            public void onDialogResult(boolean yes) {
                Notification.show(yes ? yesDelete() : noDelete());
            }}));
    }

    String yesDelete(){
        MainUI ui = (MainUI) getCurrent();
        try {
            ui.questionService.delete(selectedItem);
            String message = "Pregunta borrada";
            return message;
        }catch (SQLException e){
            return "Error";
        }
    }

     String noDelete(){
        String warning = "Operación cancelada";
        return warning;
    }

    String getShortenedQuestion(String question){
        String shortenedQuestion="";
         if (question!=null) {
             if (question.length() > 10) {
                 shortenedQuestion = question.substring(0, 10) + "... ?";
             } else {
                 shortenedQuestion = question;
             }
         }
         return shortenedQuestion;
    }

}