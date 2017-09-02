package org.tinnwork.arke.subjects;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import java.io.Serializable;
import java.sql.SQLException;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.common.YesNoDialog;

import static com.vaadin.ui.UI.getCurrent;

/**
 * Created by fran on 16/06/17.
 */
public class SubjectForm extends FormLayout implements Serializable{

    Button save = new Button("Guardar", this::save);
    private Button cancel = new Button("Cancelar", this::cancel);
    private Button lessons = new Button("Temas", this::lessons);
    Button delete = new Button("Borrar", this::delete);
    private TextField subjectTitle = new TextField("Asignatura");
    private TextField subjectCourse = new TextField("Curso");
    private Label newOrEditTitle = new Label("Añadir asignatura");
    public Subject selectedItem;

    public SubjectForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        Binder<Subject> binder = new Binder<>();
        binder.forField(subjectCourse)
                .withConverter(
                new StringToIntegerConverter("Tiene que ser un número"))
                .bind(Subject::getCourse, Subject::setCourse);

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
        subjectTitle.setWidth(300,Unit.PIXELS);
        subjectCourse.setWidth(300,Unit.PIXELS);
        save.setWidth(300,Unit.PIXELS);
        lessons.setWidth(300,Unit.PIXELS);
        cancel.setWidth(300,Unit.PIXELS);
        delete.setWidth(300,Unit.PIXELS);
        delete.setVisible(false);

        addComponents(newOrEditTitle,subjectTitle, subjectCourse, save,lessons,cancel,delete);
    }

    void newSubject() {
        selectedItem=null;

        newOrEditTitle.setValue("Añadir asignatura");
        subjectTitle.setValue("");
        subjectCourse.setValue("");

        delete.setVisible(false);
        setVisible(true);
    }

    void edit(Subject subject) {
        selectedItem=subject;
        newOrEditTitle.setValue("Editar asignatura");
        subjectTitle.setValue(subject.getSubject());
        subjectCourse.setValue(String.valueOf(subject.getCourse()));
        delete.setVisible(true);
        setVisible(subject != null);
    }

    public void save(Button.ClickEvent event) {
        MainUI ui = (MainUI) getCurrent();
        if (selectedItem == null) {
           saveNewSubject();
        } else {
            try {
                if (subjectTitle != null && !subjectTitle.getValue().equals("")) {
                    try {
                        ui.subjectService.edit(new Subject(selectedItem.getId(), subjectTitle.getValue(),
                                Integer.valueOf(subjectCourse.getValue()), selectedItem.getNumberOfQuestions()));
                        Notification notification = new Notification("Asignatura " + subjectTitle.getValue() + " guardada");
                        notification.show(Page.getCurrent());
                        selectedItem.setSubject(subjectTitle.getValue());
                        selectedItem.setCourse(Integer.valueOf(subjectCourse.getValue()));
                        ui.subject = subjectTitle.getValue();
                    } catch (SQLException e){}
                } else {
                    Notification notification = new Notification("Rellena el campo \"Asignatura\"");
                    notification.show(Page.getCurrent());
                }
            } catch (NumberFormatException e) {
                Notification notification = new Notification("Campo \"Curso\" debe ser un número");
                notification.show(Page.getCurrent());
            }
        }
    }

    void saveNewSubject(){
        MainUI ui = (MainUI) getCurrent();
        try {
            if (subjectTitle != null && !subjectTitle.getValue().equals("")) {
                Subject newSubject = new Subject(null, subjectTitle.getValue(),
                        Integer.valueOf(subjectCourse.getValue()),
                        0);
                try {
                    ui.subjectService.save(newSubject);
                    Notification notification = new Notification("Asignatura " + subjectTitle.getValue() + " añadida");
                    notification.show(Page.getCurrent());
                    setVisible(false);
                    selectedItem=newSubject;
                }catch (SQLException e){}
            } else {
                Notification notification = new Notification("Rellena el campo \"Asignatura\"");
                notification.show(Page.getCurrent());
            }
        } catch (NumberFormatException e) {
            Notification notification = new Notification("Campo \"Curso\" debe ser un número");
            notification.show(Page.getCurrent());
        }
    }


    void lessons(Button.ClickEvent event) {
        if (selectedItem==null)
            saveNewSubject();
        if (selectedItem!=null) {
            MainUI ui = (MainUI) getCurrent();
            ui.id_subject = selectedItem.getId();
            ui.subject = selectedItem.getSubject();
            ui.showLessons();
        }
    }

    public void cancel(Button.ClickEvent event) {
        setVisible(false);
    }

    void delete(Button.ClickEvent event) {
        MainUI ui= (MainUI) getCurrent();
        ui.addWindow( new YesNoDialog(
                "Borrar asignatura " + selectedItem.getSubject(),
                "¿Está seguro de que desea eliminar permanentemente la asignatura " + selectedItem.getSubject()+"?",                        new YesNoDialog.Callback() {
            public void onDialogResult(boolean yes) {
                Notification.show(yes ? yesDelete() : noDelete());
            }}));
    }

    String yesDelete(){
        MainUI ui = (MainUI) getCurrent();
        try {
            ui.subjectService.delete(selectedItem);
            String message = "Asignatura " + selectedItem.getSubject() + " borrada";
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