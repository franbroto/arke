package org.tinnwork.arke.group;

import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;


import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.PointLabels;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.base.elements.XYseries;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.Series;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.questions.Question;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;


import static com.vaadin.ui.UI.getCurrent;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Created by fran on 15/08/17.
 */
public class TestForm extends FormLayout{


    List<Question> questionList = new ArrayList<>();
    MainUI ui = (MainUI) getCurrent();
    Question currentQuestion = new Question();
    Label time;
    Label question = new Label("Pregunta");
    Label answer1 = new Label("respuesta 1");
    Label answer2 = new Label("respuesta 2");
    Label answer3 = new Label("respuesta 3");
    Label answer4 = new Label("respuesta 4");
    Image circle;
    Image square;
    Image triangle;
    Image diamond;
    DCharts chart;

    int questionId = 0;
    private final ScheduledExecutorService scheduler =
    Executors.newScheduledThreadPool(1);

    public TestForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        question.setWidth("100%");
        answer1.setWidth("100%");
        answer2.setWidth("100%");
        answer3.setWidth("100%");
        answer4.setWidth("100%");

        ThemeResource circleResource = new ThemeResource("img/circle.png");
        ThemeResource squareResource = new ThemeResource("img/square.png");
        ThemeResource triangleResource = new ThemeResource("img/triangle.png");
        ThemeResource diamondResource = new ThemeResource("img/rombo.png");
        circle = new Image("",circleResource);
        square = new Image(null, squareResource);
        triangle = new Image(null, triangleResource);
        diamond = new Image(null, diamondResource);
        circle.setVisible(false);
        square.setVisible(false);
        triangle.setVisible(false);
        diamond.setVisible(false);
        answer1.setVisible(false);
        answer2.setVisible(false);
        answer3.setVisible(false);
        answer4.setVisible(false);




        DataSeries dataSeries = new DataSeries()
                .add(2)
                .add(3)
                .add(25);

        SeriesDefaults seriesDefaults = new SeriesDefaults()
                .setRenderer(SeriesRenderers.BAR)
                .setShadow(false)
                .setPointLabels(new PointLabels()
                    .setShow(true));

        Series series = new Series()
                .addSeries(new XYseries()
                .setColor("#ff0000"))
                .addSeries(new XYseries()
                .setColor("#00ff00"));

        Axes axes = new Axes()
                .addAxis(new XYaxis()
                        .setRenderer(AxisRenderers.CATEGORY)
                        .setTicks(new Ticks()
                                .add(""))
                        .setShow(false));

        Highlighter highlighter = new Highlighter()
                .setShow(false);


        org.dussan.vaadin.dcharts.options.Grid grid= new org.dussan.vaadin.dcharts.options.Grid()
                .setDrawGridlines(false)
                .setDrawBorder(false)
                .setBackground("transparent")
                .setShadow(false);


        Options options = new Options()
                .setSeriesDefaults(seriesDefaults)
                .setSeries(series)
                .setAxes(axes)
                .setHighlighter(highlighter)
                .setGrid(grid)
                .setAnimate(true)
                .setSyncXTicks(false)
                .setSyncYTicks(false);



     //   chart = new DCharts()
     //           .setDataSeries(dataSeries)
     //           .setOptions(options)
     //   .show();

   //     chart.setWidth("50%");






    }

    private void buildLayout() {
        setMargin(false);
        setSpacing(false);
        setSizeFull();
        addStyleName("v-scrollable");
        addStyleName("test-padding");
        setHeight("100%");

        VerticalLayout mainLayout = new VerticalLayout();

        mainLayout.addComponent(question);
        mainLayout.addComponent(circle);
        mainLayout.addComponent(answer1);
        mainLayout.addComponent(square);
        mainLayout.addComponent(answer2);
        mainLayout.addComponent(triangle);
        mainLayout.addComponent(answer3);
        mainLayout.addComponent(diamond);
        mainLayout.addComponent(answer4);
   //     mainLayout.addComponent(chart);
        addComponent(mainLayout);

    }

    public void startTest(){
        getQuestions();
        setQuestionView();
    }

    private void getQuestions(){
        switch (ui.testCode) {
            case 0:
                questionList = ui.questionService.getPIRQuestions();
                break;
            case 1:
               questionList = ui.questionService.getQuestionsBySubject();
                break;
            case 2:
                try {
                    questionList = ui.questionService.readFromDB();
                } catch (SQLException e) {
                    Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
                    notification.show(Page.getCurrent());
                }
                break;
        }
        Collections.shuffle(questionList, new Random());
    }

    private void setQuestionView () {
        currentQuestion = questionList.get(questionId);
        question.setValue(currentQuestion.getQuestion());
        ArrayList<String> answers = new ArrayList<>();
        answers.add(currentQuestion.getAnswer1());
        answers.add(currentQuestion.getAnswer2());
        answers.add(currentQuestion.getAnswer3());
        if (!currentQuestion.getAnswer4().equals("")) {
            answers.add(currentQuestion.getAnswer4());
        }
        Collections.shuffle(answers, new Random());
        answer1.setValue(answers.get(0));
        answer2.setValue(answers.get(1));
        answer3.setValue(answers.get(2));
        if (!currentQuestion.getAnswer4().equals("")) {
            answer4.setValue(answers.get(3));
        }

        question.addStyleName("fadein-short");
        question.addStyleName("question");

        final Runnable showAnswers = new Runnable() {
            public void run() {
                try {
                    answer1.addStyleName("fadein");
                    answer1.addStyleName("answer");
                    answer2.addStyleName("fadein");
                    answer2.addStyleName("answer");
                    answer3.addStyleName("fadein");
                    answer3.addStyleName("answer");
                    answer4.addStyleName("fadein");
                    answer4.addStyleName("answer");

                    circle.setStyleName("shape");
                    circle.addStyleName("movein");
                    square.setStyleName("shape");
                    square.addStyleName("movein");
                    triangle.setStyleName("shape");
                    triangle.addStyleName("movein");
                    diamond.setStyleName("shape");
                    diamond.addStyleName("movein");

                    circle.setVisible(true);
                    square.setVisible(true);
                    triangle.setVisible(true);
                    diamond.setVisible(true);
                    answer1.setVisible(true);
                    answer2.setVisible(true);
                    answer3.setVisible(true);
                    answer4.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> showAnswersHandle =
                scheduler.schedule(showAnswers, 3, SECONDS);
    }


}
