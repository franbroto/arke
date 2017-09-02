package org.tinnwork.arke.subjects;

import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.io.Serializable;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.common.MainWindow;

import static com.vaadin.ui.UI.getCurrent;

/**
 * Created by fran on 16/06/17.
 */
public class SubjectView extends MainWindow implements Serializable{

    Grid<Subject> subjects = new Grid<>();
    TextField filter = new TextField();
    Button newSubject = new Button("Añadir asignatura");
    SubjectForm subjectForm = new SubjectForm();

    public SubjectView() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

        //Listeners
        newSubject.addClickListener(e -> {
            subjectForm.newSubject();
            subjects.deselectAll();
        });
        subjects.addItemClickListener(event -> subjectForm.edit(event.getItem()));
        filter.setPlaceholder("Buscar por palabras...");
        filter.addValueChangeListener(e -> refreshSubjects(e.getValue()));
        subjectForm.save.addClickListener(e -> refreshSubjects());
        subjectForm.delete.addFocusListener(e -> refreshSubjects());

        MainUI ui = (MainUI) getCurrent();
        //read all db
        try {
            ui.questionService.readFromDB();
            ui.lessonService.readFromDB();
            ui.subjectService.readFromDB();
        }catch (Exception e){
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }

        //grid
        subjects.setItems(ui.subjectService.subjects);
        subjects.addColumn(Subject::getSubject).setCaption("Asignatura").setExpandRatio(50);
        subjects.addColumn(Subject::getCourse).setCaption("Curso").setExpandRatio(1);
        subjects.addColumn(Subject::getNumberOfQuestions).setCaption("Número de preguntas").setExpandRatio(1);

        subjects.setWidth("100%");
    }

    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter, newSubject);

        actions.setMargin(false);
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);

        VerticalLayout left = new VerticalLayout(actions, subjects);
        left.setSizeFull();
        left.setMargin(false);
        subjects.setSizeFull();
        left.setExpandRatio(subjects, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, subjectForm);
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setExpandRatio(left, 1);


        addComponent(mainLayout);
        setComponentAlignment(mainLayout, Alignment.TOP_CENTER);
        setExpandRatio(mainLayout, 15.0f);
    }

    public void refreshSubjects() {
        refreshSubjects(filter.getValue());
    }

    private void refreshSubjects(String stringFilter) {
        MainUI ui = (MainUI) getCurrent();
        ui.subjectService.calculateNumberOfQuestions();
        subjects.setItems(ui.subjectService.findAll(stringFilter));

        //if the subject has been deleted
        try {
            if (ui.subjectService.findAll("id="+subjectForm.selectedItem.getId()).isEmpty()) {
                subjectForm.setVisible(false);
            }
        }catch (Exception e){}
    }
}