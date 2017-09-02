package org.tinnwork.arke;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.UIEvents;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;


import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.tinnwork.arke.auth.AuthService;
import org.tinnwork.arke.auth.LoginView;
import org.tinnwork.arke.glossary.GlossaryService;
import org.tinnwork.arke.glossary.GlossaryView;
import org.tinnwork.arke.group.GroupPlayView;
import org.tinnwork.arke.lessons.LessonService;
import org.tinnwork.arke.lessons.LessonView;
import org.tinnwork.arke.menu.MainMenuView;
import org.tinnwork.arke.questions.QuestionService;
import org.tinnwork.arke.questions.QuestionView;
import org.tinnwork.arke.subjects.SubjectService;
import org.tinnwork.arke.subjects.SubjectView;



@Theme("mytheme")
public class MainUI extends UI implements Serializable {

    public String currentScreen;
    final public String LOGIN = "Login";
    final public String MAIN_MENU = "Main menu";
    final public String SUBJECTS = "Subjects";
    final public String LESSONS = "Lessons";
    final public String QUESTIONS = "Questions";
    final public String GLOSSARY = "Glossary";
    final public String GROUP_PLAY = "Group Play";
    public Long id_subject;
    public Long id_lesson;
    public String subject;
    public String lesson;
    public int testCode;//0-PIR, 1-Subject, 2-Lesson
    public Statement st;
    public SubjectService subjectService;
    public LessonService lessonService;
    public QuestionService questionService;
    public GlossaryService glossaryService;

    transient Connection conn=null;
   
    @Override
    protected void init(VaadinRequest vaadinRequest) {
    		
        subjectService = new SubjectService();
        lessonService = new LessonService();
        questionService = new QuestionService();
        glossaryService = new GlossaryService();
        
       if (!AuthService.isAuthenticated()) {
            showLogin();
        } else {
            showMainMenu();
        }
    }

    public void showLogin() {
        currentScreen=LOGIN;
        setContent(new LoginView());
    }

    public void showMainMenu() {
        currentScreen=MAIN_MENU;
        setContent(new MainMenuView());
    }

    public void showSubjects() {
        currentScreen=SUBJECTS;
        setContent(new SubjectView());
    }

    public void showLessons() {
        currentScreen=LESSONS;
        setContent(new LessonView());
    }

    public void showQuestions() {
        currentScreen=QUESTIONS;
        setContent(new QuestionView());
    }

    public void showGlossary() {
        currentScreen=GLOSSARY;
        setContent(new GlossaryView());
    }

    public void showGroupPlay() {
        currentScreen=GROUP_PLAY;
        setContent(new GroupPlayView());
    }


    public Connection getConnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://tinnwork.org:3306/fran_arke";
            Connection conn = DriverManager.getConnection(url, "fran_admin", "AESdEA17");
            return conn;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

   
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MainUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
