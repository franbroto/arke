package org.tinnwork.arke.subjects;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

import org.apache.commons.beanutils.BeanUtils;
import org.tinnwork.arke.MainUI;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


import static com.vaadin.ui.UI.getCurrent;


public class SubjectService implements Serializable{

    public ArrayList<Subject> subjects = new ArrayList();
    transient public Statement st;
    
    
    public List<Subject> readFromDB()throws SQLException{
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
       try {
            ResultSet srs = st.executeQuery("SELECT * FROM subject");
            subjects.removeAll(subjects);
            while (srs.next()) {
                Subject subject = new Subject();
                subject.setSubject(srs.getString("subject"));
                subject.setCourse(srs.getInt("course"));
                subject.setId(srs.getLong("id"));
                subjects.add(subject);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        calculateNumberOfQuestions();
        st.close();
        return subjects;
    }

    public void calculateNumberOfQuestions(){
        MainUI ui = (MainUI) getCurrent();
        int i;
        for (i=0;i<subjects.size();i++){
            int numberOfCuestionsBySubject=0;
            int j;
            for (j=0;j<ui.lessonService.allLessons.size();j++) {
                int numberOfCuestionsByLesson = 0;
                int k;
                for (k = 0; k < ui.questionService.allQuestions.size(); k++) {
                    if (ui.questionService.allQuestions.get(k).getIdLesson() == ui.lessonService.allLessons.get(j).getId())
                        numberOfCuestionsByLesson=numberOfCuestionsByLesson+1;
                }
                ui.lessonService.allLessons.get(j).setNumberOfQuestions(numberOfCuestionsByLesson);
                if (ui.lessonService.allLessons.get(j).getIdSubject()==subjects.get(i).getId())
                    numberOfCuestionsBySubject=numberOfCuestionsBySubject+ui.lessonService.allLessons.get(j).getNumberOfQuestions();
            }
            subjects.get(i).setNumberOfQuestions(numberOfCuestionsBySubject);
        }
    }

    public synchronized List<Subject> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        int i;
        for (i = 0; i < subjects.size(); i++) {
            Subject subject = (Subject) subjects.get(i);
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || subject.toString().toLowerCase()
                        .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(subject.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(SubjectService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return arrayList;
    }

    public synchronized long count() {
        return subjects.size();
    }

    public synchronized void save(Subject entry)throws SQLException {
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        try {
            entry = (Subject) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("INSERT INTO subject (subject,course)"+" VALUES ('"+entry.getSubject()+"',"+entry.getCourse()+")");
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
    }

    public synchronized void edit(Subject entry) throws SQLException{
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement(); 
        try {
            entry = (Subject) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("UPDATE subject"+" SET subject='"+entry.getSubject()+"', course="+entry.getCourse()+" WHERE id="+entry.getId());
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
    }


    public synchronized void delete(Subject entry)throws SQLException {
        MainUI ui = (MainUI) getCurrent();
        st = ui.getConnection().createStatement();
        try {
            entry = (Subject) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("DELETE FROM subject"+" WHERE id="+entry.getId());
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
    }
}


