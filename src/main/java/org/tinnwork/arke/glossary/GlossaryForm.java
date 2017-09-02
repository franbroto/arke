package org.tinnwork.arke.glossary;


import com.vaadin.event.ShortcutAction;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import java.io.Serializable;
import java.sql.SQLException;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.common.YesNoDialog;

import static com.vaadin.ui.UI.getCurrent;

/**
* Created by fran on 16/06/17.
*/
public class GlossaryForm extends FormLayout {

   Button save = new Button("Guardar", this::save);
   private Button cancel = new Button("Cancelar", this::cancel);
   Button delete = new Button("Borrar", this::delete);
   private TextField term = new TextField("Término");
   private TextField definition = new TextField("Definición");
   private Label newOrEditTitle = new Label("Añadir término");
   public Glossary selectedItem;

   public GlossaryForm() {
       configureComponents();
       buildLayout();
   }

   private void configureComponents() {
       save.setStyleName(ValoTheme.BUTTON_PRIMARY);
       save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
       setVisible(false);
   }

   private void buildLayout() {
       setSizeUndefined();
       setMargin(true);
       this.addStyleName("v-scrollable");
       this.setHeight("100%");

       newOrEditTitle.addStyleName(ValoTheme.LABEL_H2);
       term.setWidth(300,Unit.PIXELS);
       definition.setWidth(300,Unit.PIXELS);
       save.setWidth(300,Unit.PIXELS);
       cancel.setWidth(300,Unit.PIXELS);
       delete.setWidth(300,Unit.PIXELS);
       delete.setVisible(false);

       addComponents(newOrEditTitle,term, definition, save, cancel,delete);
   }

   void newGlossaryTerm() {
       selectedItem=null;

       newOrEditTitle.setValue("Añadir término");
       term.setValue("");
       definition.setValue("");

       delete.setVisible(false);
       setVisible(true);
   }

   void edit(Glossary glossaryTerm) {
       selectedItem=glossaryTerm;
       newOrEditTitle.setValue("Editar término");
       term.setValue(glossaryTerm.getTerm());
       definition.setValue(glossaryTerm.getDefinition());
       delete.setVisible(true);
       setVisible(glossaryTerm != null);
   }

   public void save(Button.ClickEvent event) {
       MainUI ui = (MainUI) getCurrent();
       if (selectedItem == null) {
           saveNewTerm();
       } else {
           if (term != null && !term.getValue().equals("")) {
               if (definition != null && !definition.getValue().equals("")) {
                   try {
                       ui.glossaryService.edit(new Glossary(selectedItem.getId(), term.getValue(),
                               definition.getValue()));
                       Notification notification = new Notification("Término " + term.getValue() + " guardado");
                       notification.show(Page.getCurrent());
                       selectedItem.setTerm(term.getValue());
                       selectedItem.setDefinition(definition.getValue());
                   }catch (SQLException e){}
               } else {
                   Notification notification = new Notification("Rellena el campo \"Definición\"");
                   notification.show(Page.getCurrent());
               }
           } else {
               Notification notification = new Notification("Rellena el campo \"Término\"");
               notification.show(Page.getCurrent());
           }
       }
   }


   void saveNewTerm(){
       MainUI ui = (MainUI) getCurrent();
       if (term != null && !term.getValue().equals("")) {
           if (definition != null && !definition.getValue().equals("")) {
               Glossary newGlossaryTerm = new Glossary(null, term.getValue(), definition.getValue());
               try {
                   ui.glossaryService.save(newGlossaryTerm);
                   Notification notification = new Notification("Término " + term.getValue() + " añadido");
                   notification.show(Page.getCurrent());
                   setVisible(false);
                   selectedItem=newGlossaryTerm;
               }catch (SQLException e){}
           } else {
               Notification notification = new Notification("Rellena el campo \"Definición\"");
               notification.show(Page.getCurrent());
           }
       } else {
           Notification notification = new Notification("Rellena el campo \"Término\"");
           notification.show(Page.getCurrent());
       }
   }

   public void cancel(Button.ClickEvent event) {
       setVisible(false);
   }

   void delete(Button.ClickEvent event) {
       MainUI ui= (MainUI) getCurrent();
       ui.addWindow( new YesNoDialog(
               "Borrar término " + selectedItem.getTerm(),
               "¿Está seguro de que desea eliminar permanentemente el término " + selectedItem.getTerm()+"?",                        new YesNoDialog.Callback() {
           public void onDialogResult(boolean yes) {
               Notification.show(yes ? yesDelete() : noDelete());
           }}));
   }

   String yesDelete(){
       MainUI ui = (MainUI) getCurrent();
       try {
           ui.glossaryService.delete(selectedItem);
           String message = "Término " + selectedItem.getTerm() + " borrado";
           return message;
       }catch (SQLException e){
           return "Error";
       }
   }

    String noDelete(){
       String warning = "Operación cancelada";
       return warning;
   }

}