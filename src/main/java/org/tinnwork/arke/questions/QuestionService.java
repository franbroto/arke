package org.tinnwork.arke.questions;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import org.apache.commons.beanutils.BeanUtils;
import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.lessons.Lesson;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import static com.vaadin.ui.UI.getCurrent;


public class QuestionService implements Serializable{

    private ArrayList<Question> questions = new ArrayList();
    public ArrayList<Question> allQuestions = new ArrayList();
    transient  public Statement st;


    public List<Question> readFromDB() throws SQLException {
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        String url = "jdbc:mysql://tinnwork.org:3306/fran_arke";
        Connection conn = DriverManager.getConnection(url, "fran_admin", "AESdEA17");
        st=conn.createStatement();
        try {
            ResultSet srs = st.executeQuery("SELECT * FROM question");
            allQuestions.removeAll(allQuestions);
            while (srs.next()) {
                Question question = new Question();
                question.setQuestion(srs.getString("question"));
                question.setVPir(srs.getBoolean("v_pir"));
                question.setAnswer1(srs.getString("answer1"));
                question.setAnswer2(srs.getString("answer2"));
                question.setAnswer3(srs.getString("answer3"));
                question.setAnswer4(srs.getString("answer4"));
                question.setVAnswer1(srs.getBoolean("v_answer1"));
                question.setVAnswer2(srs.getBoolean("v_answer2"));
                question.setVAnswer3(srs.getBoolean("v_answer3"));
                question.setVAnswer4(srs.getBoolean("v_answer4"));
                question.setExposition(srs.getString("exposition"));
                question.setIdLesson(srs.getLong("id_lesson"));
                question.setId(srs.getLong("id"));
                allQuestions.add(question);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        st.close();
        conn.close();
        return questionsByLesson(ui.id_lesson);
    }


    public List<Question> questionsByLesson(Long id_lesson) {
        int i;
        questions.removeAll(questions);
        for (i = 0; i < allQuestions.size(); i++) {
            Question question = (Question) allQuestions.get(i);
            try {
                boolean passesFilter = question.toString().toLowerCase()
                        .contains(("id tema=" + id_lesson).toLowerCase());
                if (passesFilter) {
                    questions.add(question.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(QuestionService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return questions;
    }

    public synchronized List<Question> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        int i;
        for (i = 0; i < questions.size(); i++) {
            Question question = (Question) questions.get(i);
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || question.toString().toLowerCase()
                        .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(question.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(QuestionService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return arrayList;
    }

    public synchronized long count() {
        return questions.size();
    }

    public synchronized void save(Question entry) throws SQLException {
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        String url = "jdbc:mysql://tinnwork.org:3306/fran_arke";
        Connection conn = DriverManager.getConnection(url, "fran_admin", "AESdEA17");
        st=conn.createStatement();
        try {
            entry = (Question) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("INSERT INTO question (question,v_pir,answer1,answer2,answer3,answer4,v_answer1,v_answer2,v_answer3,v_answer4,exposition,id_lesson)" +
                    " VALUES ('" + entry.getQuestion() + "'," + entry.getVPir() + ",'" + entry.getAnswer1() + "','" + entry.getAnswer2() + "','" + entry.getAnswer3() + "','" + entry.getAnswer4() +
                    "'," + entry.getVAnswer1() + "," + entry.getVAnswer2() + "," + entry.getVAnswer3() + "," + entry.getVAnswer4() + ",'" + entry.getExposition() + "'," + ui.id_lesson + ")");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            readFromDB();
        } catch (SQLException e) {
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        st.close();
        conn.close();
       
    }

    public synchronized void edit(Question entry)  throws SQLException{
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        String url = "jdbc:mysql://tinnwork.org:3306/fran_arke";
        Connection conn = DriverManager.getConnection(url, "fran_admin", "AESdEA17");
        st=conn.createStatement();
        try {
            entry = (Question) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("UPDATE question" + " SET question='" + entry.getQuestion() +  "',v_pir=" + entry.getVPir() +
                    ",answer1='" + entry.getAnswer1() + "',answer2='" + entry.getAnswer2() + "',answer3='" + entry.getAnswer3() + "',answer4='" + entry.getAnswer4() +
                    "',v_answer1=" + entry.getVAnswer1() + ",v_answer2=" + entry.getVAnswer2() + ",v_answer3=" + entry.getVAnswer3() + ",v_answer4=" + entry.getVAnswer4() +
                    ",exposition='" + entry.getExposition() +
                    "' WHERE id=" + entry.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            readFromDB();
        } catch (SQLException e) {
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        st.close();
        conn.close();
    }


    public synchronized void delete(Question entry) throws SQLException {
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        String url = "jdbc:mysql://tinnwork.org:3306/fran_arke";
        Connection conn = DriverManager.getConnection(url, "fran_admin", "AESdEA17");
        st=conn.createStatement();try {
            entry = (Question) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("DELETE FROM question" + " WHERE id=" + entry.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            readFromDB();
        } catch (SQLException e) {
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        st.close();
        conn.close();
    }



    /***************************************************
     * test
     */
    public List<Question>  getPIRQuestions(){
        if (allQuestions.size()==0) {
            try {
                readFromDB();
            } catch (SQLException e) {
                Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
                notification.show(Page.getCurrent());
            }
        }
        List<Question> pirQuestionsList = new ArrayList<>();
        int i;
        for (i=1;i<allQuestions.size();i++){
            if(allQuestions.get(i).getVPir())
                pirQuestionsList.add(allQuestions.get(i));
        }
        return pirQuestionsList;
    }


    public List<Question>  getQuestionsBySubject(){
        MainUI ui = (MainUI) getCurrent();
        if (allQuestions.size()==0) {
            try {
                readFromDB();
            } catch (SQLException e) {
                Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
                notification.show(Page.getCurrent());
            }
        }
        List<Lesson> lessonsList=new ArrayList<>();
        try {
            lessonsList= ui.lessonService.readFromDB();
        } catch (SQLException e) {
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        List<Question> questionsList = new ArrayList<>();
        int i;
        for (i=0;i<lessonsList.size();i++){
            questionsList.addAll(questionsByLesson(lessonsList.get(i).getId()));
        }
        return questionsList;
    }





}

