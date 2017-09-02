package org.tinnwork.arke.lessons;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

import org.apache.commons.beanutils.BeanUtils;
import org.tinnwork.arke.MainUI;

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


public class LessonService implements Serializable{

	private static final long serialVersionUID = 1L;
	public ArrayList<Lesson> lessons = new ArrayList<Lesson>();
    public ArrayList<Lesson> allLessons = new ArrayList<Lesson>();
    transient public Statement st;

    public List<Lesson> readFromDB() throws SQLException{
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        String url = "jdbc:mysql://tinnwork.org:3306/fran_arke";
        Connection conn = DriverManager.getConnection(url, "fran_admin", "AESdEA17");
        st=conn.createStatement();
        try {
            ResultSet srs = st.executeQuery("SELECT * FROM lesson");
            allLessons.removeAll(allLessons);
            while (srs.next()) {
            	
            	
            	
                Lesson lesson = new Lesson();
                lesson.setLesson(srs.getString("lesson"));
                lesson.setIdSubject(srs.getLong("id_subject"));
                lesson.setId(srs.getLong("id"));
                allLessons.add(lesson);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        st.close();
        conn.close();
        return lessonsBySubject(ui.id_subject);
    }

    public void calculateNumberOfQuestions() {
        MainUI ui = (MainUI) getCurrent();
        int i;
        for (i = 0; i < lessons.size(); i++) {
            int numberOfCuestionsByLesson = 0;
            int k;
            for (k = 0; k < ui.questionService.allQuestions.size(); k++) {
                if (ui.questionService.allQuestions.get(k).getIdLesson() == lessons.get(i).getId())
                    numberOfCuestionsByLesson = numberOfCuestionsByLesson + 1;
            }
            lessons.get(i).setNumberOfQuestions(numberOfCuestionsByLesson);
        }
    }


    public List<Lesson> lessonsBySubject(Long id_subject){
        int i;
        lessons.removeAll(lessons);
        for (i = 0; i < allLessons.size(); i++) {
            Lesson lesson = (Lesson) allLessons.get(i);
            try {
                boolean passesFilter = lesson.toString().toLowerCase()
                        .contains(("id asignatura="+id_subject).toLowerCase());
                if (passesFilter) {
                    lessons.add(lesson.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(LessonService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        calculateNumberOfQuestions();
        return lessons;
    }

    public synchronized List<Lesson> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        int i;
        for (i = 0; i < lessons.size(); i++) {
            Lesson lesson = (Lesson) lessons.get(i);
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || lesson.toString().toLowerCase()
                        .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(lesson.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(LessonService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return arrayList;
    }

    public synchronized long count() {
        int i;
        long count = 0;
        for (i=0;i<lessons.size();i++){
            Lesson lesson = (Lesson)lessons.get(i);
            count=count+lesson.getNumberOfQuestions();
        }
        return count+1;
    }

    public synchronized void save(Lesson entry)throws SQLException {

        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        String url = "jdbc:mysql://tinnwork.org:3306/fran_arke";
        Connection conn = DriverManager.getConnection(url, "fran_admin", "AESdEA17");
        st=conn.createStatement();
        try {
           entry = (Lesson) BeanUtils.cloneBean(entry);
           
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("INSERT INTO lesson (lesson,id_subject)"+" VALUES ('"+entry.getLesson()+"',"+ui.id_subject+")");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            readFromDB();
        }catch (SQLException e){
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        st.close();
        conn.close();
    }

    public synchronized void edit(Lesson entry) throws SQLException {
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        String url = "jdbc:mysql://tinnwork.org:3306/fran_arke";
        Connection conn = DriverManager.getConnection(url, "fran_admin", "AESdEA17");
        st=conn.createStatement();
        try {
           entry = (Lesson) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("UPDATE lesson"+" SET lesson='"+entry.getLesson()+"' WHERE id="+entry.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            readFromDB();
        }catch (SQLException e){
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        st.close();
        conn.close();
    }


    public synchronized void delete(Lesson entry) throws SQLException{
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        String url = "jdbc:mysql://tinnwork.org:3306/fran_arke";
        Connection conn = DriverManager.getConnection(url, "fran_admin", "AESdEA17");
        st=conn.createStatement();
        try {
            entry = (Lesson) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("DELETE FROM lesson"+" WHERE id="+entry.getId());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        try {
            readFromDB();
        }catch (SQLException e){
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        st.close();
        conn.close();
    }
}


