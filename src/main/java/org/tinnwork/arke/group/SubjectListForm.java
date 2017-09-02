package org.tinnwork.arke.group;

import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;
import static com.vaadin.ui.UI.getCurrent;

import java.io.Serializable;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.lessons.Lesson;
import org.tinnwork.arke.subjects.Subject;


/**
 * Created by fran on 16/06/17.
 */
public class SubjectListForm extends FormLayout implements Serializable{

    private Label selectGame = new Label("Configurar Test");
    Grid<Subject> subjects = new Grid<>();
    Grid<Lesson> lessons = new Grid<>();
    TextField numberOfQuestions = new TextField("Nº de preguntas");
    TextField time = new TextField("Tiempo (segundos)");
    Button startButton = new Button("Empezar: Test Global (PIR)", this::start);
    Button backToSubjectsButton = new Button("< Asignaturas", this::backToSubjects);

    public SubjectListForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        MainUI ui = (MainUI) getCurrent();
        subjects.setWidth("100%");
        subjects.setHeight(340,Unit.PIXELS);
        subjects.addStyleName("grid-transparent-small");
        lessons.setWidth("100%");
        lessons.setHeight(340,Unit.PIXELS);
        lessons.addStyleName("grid-transparent-small");
        selectGame.addStyleName("title");
        selectGame.setWidth("100%");
        startButton.setWidth("100%");
        startButton.addStyleName("button");
        //startButton.setEnabled(false);
        ui.testCode=0;

        subjects.addColumn(Subject::getSubject).setCaption("Asignaturas");
        lessons.addColumn(Lesson::getLesson);
        try {
            ui.subjectService.readFromDB();
        }catch (Exception e){
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        subjects.setItems(ui.subjectService.subjects);
        numberOfQuestions.setValue("10");
        time.setValue("20");

    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);
        addStyleName("v-scrollable");
        setHeight("100%");
        lessons.setVisible(false);
        backToSubjectsButton.setVisible(false);

        numberOfQuestions.setWidth("100%");
        numberOfQuestions.addStyleName("field");
        time.setWidth("100%");
        time.addStyleName("field");
        HorizontalLayout configGame = new HorizontalLayout(numberOfQuestions,time);
        configGame.setWidth("100%");
        configGame.addStyleName("caption");
        backToSubjectsButton.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        backToSubjectsButton.addStyleName("back-button");
        HorizontalLayout subjectsLayout = new HorizontalLayout(backToSubjectsButton,subjects,lessons);
        subjectsLayout.setWidth("100%");
        subjectsLayout.setExpandRatio(backToSubjectsButton,3.0f);
        subjectsLayout.setExpandRatio(subjects,10.0f);
        subjectsLayout.setExpandRatio(lessons,10.0f);
        addComponents(selectGame,subjectsLayout,configGame,startButton);
    }

    void selectSubject(Subject selectedItem) {
        MainUI ui = (MainUI) getCurrent();
        ui.id_subject = selectedItem.getId();
        ui.subject = selectedItem.getSubject();
        try {
            ui.lessonService.readFromDB();
        }catch (Exception e){}
        lessons.setItems(ui.lessonService.lessons);
        startButton.setCaption("Empezar: Test Global "+ui.subject);
        lessons.getColumns().get(0).setCaption(ui.subject);
        subjects.setVisible(false);
        lessons.setVisible(true);
        backToSubjectsButton.setVisible(true);
        ui.testCode=1;
    }

    void selectLesson(Lesson selectedItem) {
        MainUI ui = (MainUI) getCurrent();
        ui.id_lesson = selectedItem.getId();
        ui.lesson = selectedItem.getLesson();
        startButton.setCaption("Empezar: Test "+ui.lesson);
        lessons.getColumns().get(0).setCaption(ui.subject);
        ui.testCode=2;
    }

    private void backToSubjects(Button.ClickEvent event){
        MainUI ui = (MainUI) getCurrent();
        startButton.setCaption("Empezar: Test Global (PIR)");
        subjects.setVisible(true);
        lessons.setVisible(false);
        backToSubjectsButton.setVisible(false);
        ui.testCode=0;
    }


    private void start(Button.ClickEvent event) {
    }
}