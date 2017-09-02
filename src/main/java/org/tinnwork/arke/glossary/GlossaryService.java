package org.tinnwork.arke.glossary;


import com.vaadin.server.Page;
import com.vaadin.ui.Notification;

import org.apache.commons.beanutils.BeanUtils;
import org.tinnwork.arke.MainUI;

import static com.vaadin.ui.UI.getCurrent;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GlossaryService  implements Serializable{

	public ArrayList<Glossary> terms = new ArrayList<Glossary>();
    transient public Statement st;
    transient Connection conn;
    
    public List<Glossary> readFromDB()throws SQLException{
    		MainUI ui = (MainUI) getCurrent();
        st =ui.getConnection().createStatement();
        try {
            ResultSet srs = st.executeQuery("SELECT * FROM glossary");
            terms.removeAll(terms);
            while (srs.next()) {
                Glossary glossaryTerm = new Glossary();
                glossaryTerm.setTerm(srs.getString("term"));
                glossaryTerm.setDefinition(srs.getString("definition"));
                glossaryTerm.setId(srs.getLong("id"));
                terms.add(glossaryTerm);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Notification notification = new Notification("Ha habído un problema al conectar con la base de datos. Inténtalo otra vez más tarde");
            notification.show(Page.getCurrent());
        }
        st.close();
        return terms;
    }

    public synchronized List<Glossary> findAll(String stringFilter) {
        ArrayList arrayList = new ArrayList();
        arrayList.clear();
        int i;
        for (i = 0; i < terms.size(); i++) {
            Glossary glossaryTerm = (Glossary) terms.get(i);
            try {
                boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
                        || glossaryTerm.toString().toLowerCase()
                        .contains(stringFilter.toLowerCase());
                if (passesFilter) {
                    arrayList.add(glossaryTerm.clone());
                }
            } catch (CloneNotSupportedException ex) {
                Logger.getLogger(GlossaryService.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
        return arrayList;
    }

    public synchronized long count() {
        return terms.size();
    }

    public synchronized void save(Glossary entry)throws SQLException {
        
    	MainUI ui = (MainUI) getCurrent();
        st =ui.getConnection().createStatement();
       try {
            entry = (Glossary) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("INSERT INTO glossary (term,definition)"+" VALUES ('"+entry.getTerm()+"','"+entry.getDefinition()+"')");
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

    public synchronized void edit(Glossary entry)throws SQLException {
    	MainUI ui = (MainUI) getCurrent();
        st =ui.getConnection().createStatement();
        try {
            entry = (Glossary) BeanUtils.cloneBean(entry);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("UPDATE glossary"+" SET term='"+entry.getTerm()+"', definition='"+entry.getDefinition()+"' WHERE id="+entry.getId());
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


    public synchronized void delete(Glossary entry) throws SQLException {
    	MainUI ui = (MainUI) getCurrent();
        st =ui.getConnection().createStatement();
        try {
            entry = (Glossary) BeanUtils.cloneBean(entry);    
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        try {
            st.executeUpdate("DELETE FROM glossary" + " WHERE id=" + entry.getId());
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
    }
	


  }


