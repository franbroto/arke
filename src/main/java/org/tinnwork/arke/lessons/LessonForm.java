package org.tinnwork.arke.lessons;


import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.common.YesNoDialog;

import static com.vaadin.ui.UI.getCurrent;

/**
 * Created by fran on 16/06/17.
 */
public class LessonForm extends FormLayout {

    Button save = new Button("Guardar", this::save);
    private Button cancel = new Button("Cancelar", this::cancel);
    private Button questions = new Button("Preguntas", this::questions);
    Button delete = new Button("Borrar", this::delete);
    private TextField lessonTitle = new TextField("Tema");
    private Label newOrEditTitle = new Label("Añadir tema");
    public Lesson selectedItem;

    public LessonForm() {
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
        lessonTitle.setWidth(300,Unit.PIXELS);
        save.setWidth(300,Unit.PIXELS);
        questions.setWidth(300,Unit.PIXELS);
        cancel.setWidth(300,Unit.PIXELS);
        delete.setWidth(300,Unit.PIXELS);
        delete.setVisible(false);

        addComponents(newOrEditTitle,lessonTitle,save,questions,cancel,delete);
    }

    void newLesson() {
        selectedItem=null;

        newOrEditTitle.setValue("Añadir tema");
        lessonTitle.setValue("");

        delete.setVisible(false);
        setVisible(true);
    }

    void edit(Lesson lesson) {
        selectedItem=lesson;
        newOrEditTitle.setValue("Editar tema");
        lessonTitle.setValue(lesson.getLesson());
        delete.setVisible(true);
        setVisible(lesson != null);
    }

    public void save(Button.ClickEvent event) {
        MainUI ui = (MainUI) getCurrent();
        if (selectedItem == null) {
           saveNewLesson();
        } else {
            if (lessonTitle != null && !lessonTitle.getValue().equals("")) {
                try {
                    ui.lessonService.edit(new Lesson(selectedItem.getId(), lessonTitle.getValue(),
                            selectedItem.getIdSubject(), selectedItem.getNumberOfQuestions()));
                    Notification notification = new Notification("Tema " + lessonTitle.getValue() + " guardado");
                    notification.show(Page.getCurrent());
                    selectedItem.setLesson(lessonTitle.getValue());
                    ui.lesson = lessonTitle.getValue();
                }catch (SQLException e){}
            } else {
                Notification notification = new Notification("Rellena el campo \"Tema\"");
                notification.show(Page.getCurrent());
            }
        }
    }

    private void saveNewLesson(){
        MainUI ui = (MainUI) getCurrent();
        if (lessonTitle != null && !lessonTitle.getValue().equals("")) {
            if (ui.id_subject==null) {
                if (ui.subjectService.findAll("asignatura=" + ui.subject).size() > 0) {
                    int index = ui.subjectService.findAll("asignatura=" + ui.subject).size() - 1;
                    ui.id_subject = ui.subjectService.findAll("asignatura=" + ui.subject).get(index).getId();
                }
            }
            Lesson newLesson = new Lesson(null, lessonTitle.getValue(),
                    ui.id_subject, 0);
            try {
                ui.lessonService.save(newLesson);
                Notification notification = new Notification("Tema " + lessonTitle.getValue() + " añadido");
                notification.show(Page.getCurrent());
                setVisible(false);
                selectedItem=newLesson;
            } catch (SQLException e) {
            }
        } else {
            Notification notification = new Notification("Rellena el campo \"Tema\"");
            notification.show(Page.getCurrent());
        }
    }

    void questions(Button.ClickEvent event) {
        if (selectedItem==null) saveNewLesson();
        if (selectedItem!=null) {
            MainUI ui = (MainUI) getCurrent();
            ui.id_lesson = selectedItem.getId();
            ui.lesson = selectedItem.getLesson();
            ui.showQuestions();
        }
    }

    public void cancel(Button.ClickEvent event) {
        setVisible(false);
    }

    void delete(Button.ClickEvent event) {
        MainUI ui= (MainUI) getCurrent();
        ui.addWindow( new YesNoDialog(
                "Borrar tema " + selectedItem.getLesson(),
                "¿Está seguro de que desea eliminar permanentemente el tema " + selectedItem.getLesson()+"?",                        new YesNoDialog.Callback() {
            public void onDialogResult(boolean yes) {
                Notification.show(yes ? yesDelete() : noDelete());
            }}));
    }

    String yesDelete(){
        MainUI ui = (MainUI) getCurrent();
        try {
            ui.lessonService.delete(selectedItem);
            String message = "Tema " + selectedItem.getLesson() + " borrado";
            return message;
        }catch (SQLException e){
            return "Error";
        }
    }

     String noDelete(){
        String warning = "Operación cancelada";
        return warning;
    }

}