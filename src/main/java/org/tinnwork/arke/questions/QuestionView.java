package org.tinnwork.arke.questions;


import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

import java.io.File;
import java.sql.SQLException;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.common.MainWindow;

import static com.vaadin.ui.UI.getCurrent;

/**
 * Created by fran on 16/06/17.
 */
public class QuestionView extends MainWindow{

    Grid<Question> questions = new Grid<>();
    TextField filter = new TextField();
    Button newQuestion = new Button("Añadir pregunta");
    UploadFile uploadFile = new UploadFile();
    Upload upload = new Upload(null, uploadFile);
    QuestionForm questionForm = new QuestionForm();
    File file;
    HorizontalLayout mainLayout;

    public QuestionView() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

        MainUI ui = (MainUI) getCurrent();
        //Listeners
        newQuestion.addClickListener(e -> {
            questionForm.newQuestion();
            questions.deselectAll();
        });

        upload.setButtonCaption("Importar preguntas");
        upload.addSucceededListener(uploadFile);

        questions.addItemClickListener(event -> questionForm.edit(event.getItem()));
        filter.setPlaceholder("Buscar por palabras...");
        filter.addValueChangeListener(e -> refreshQuestions(e.getValue()));
        questionForm.save.addClickListener(e -> refreshQuestions());
        questionForm.delete.addFocusListener(e -> refreshQuestions());
        upload.addFinishedListener(e -> refreshQuestions());

        //grid
        try {
            questions.setItems(ui.questionService.readFromDB());
        } catch (SQLException e){
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        questions.addColumn(Question::getQuestion).setCaption("Pregunta").setExpandRatio(50);

        questions.setWidth("100%");
    }

    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter, newQuestion,upload);

        actions.setMargin(false);
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);

        VerticalLayout left = new VerticalLayout(actions, questions);
        left.setSizeFull();
        left.setMargin(false);
        questions.setSizeFull();
        left.setExpandRatio(questions, 1);

        mainLayout = new HorizontalLayout(left, questionForm);
       // mainLayout.setSizeFull();
        mainLayout.setWidth("100%");
        mainLayout.setHeight("100%");
        mainLayout.setMargin(false);
        mainLayout.setExpandRatio(left, 1);

        addComponent(mainLayout);
        setComponentAlignment(mainLayout, Alignment.TOP_CENTER);
        setExpandRatio(mainLayout, 15.0f);
    }

    public void refreshQuestions() {
        MainUI ui = (MainUI) getCurrent();
        ui.questionService.questionsByLesson(ui.id_lesson);
        refreshQuestions(filter.getValue());
    }

    private void refreshQuestions(String stringFilter) {
        MainUI ui = (MainUI) getCurrent();
        questions.setItems(ui.questionService.findAll(stringFilter));

        //if the question has been deleted
        try {
            if (ui.questionService.findAll("id="+questionForm.selectedItem.getId()).isEmpty()) {
                questionForm.setVisible(false);
            }
        }catch (Exception e){}
    }

}