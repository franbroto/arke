package org.tinnwork.arke.lessons;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;


import static com.vaadin.ui.UI.getCurrent;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.common.MainWindow;

/**
 * Created by fran on 16/06/17.
 */
public class LessonView extends MainWindow {

	private static final long serialVersionUID = 1L;
	Grid<Lesson> lessons = new Grid<>();
    TextField filter = new TextField();
    Button newLesson = new Button("Añadir tema");
    LessonForm lessonForm = new LessonForm();

    public LessonView() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {

        //Listeners
        newLesson.addClickListener(e -> {
            lessonForm.newLesson();
            lessons.deselectAll();
        });
        lessons.addItemClickListener(event -> lessonForm.edit(event.getItem()));
        filter.setPlaceholder("Buscar por palabras...");
        filter.addValueChangeListener(e -> refreshLessons(e.getValue()));
        lessonForm.save.addClickListener(e -> refreshLessons());
        lessonForm.delete.addFocusListener(e -> refreshLessons());

        //grid
        MainUI ui = (MainUI) getCurrent();
        try {
            ui.lessonService.readFromDB();
            ui.questionService.readFromDB();
        }catch (Exception e){}

        lessons.setItems(ui.lessonService.lessons);
        lessons.addColumn(Lesson::getLesson).setCaption("Tema").setExpandRatio(50);
        lessons.addColumn(Lesson::getNumberOfQuestions).setCaption("Número de preguntas").setExpandRatio(1);

        lessons.setWidth("100%");
    }

    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter, newLesson);

        actions.setMargin(false);
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);

        VerticalLayout left = new VerticalLayout(actions, lessons);
        left.setSizeFull();
        left.setMargin(false);
        lessons.setSizeFull();
        left.setExpandRatio(lessons, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, lessonForm);
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setExpandRatio(left, 1);


        addComponent(mainLayout);
        setComponentAlignment(mainLayout, Alignment.TOP_CENTER);
        setExpandRatio(mainLayout, 15.0f);
    }

    public void refreshLessons() {
        MainUI ui = (MainUI) getCurrent();
        ui.lessonService.lessonsBySubject(ui.id_subject);
        refreshLessons(filter.getValue());
    }

    private void refreshLessons(String stringFilter) {
        MainUI ui = (MainUI) getCurrent();
        lessons.setItems(ui.lessonService.findAll(stringFilter));

        //if the lesson has been deleted
        try {
            if (ui.lessonService.findAll("id="+lessonForm.selectedItem.getId()).isEmpty()) {
                lessonForm.setVisible(false);
            }
        }catch (Exception e){}
    }
}