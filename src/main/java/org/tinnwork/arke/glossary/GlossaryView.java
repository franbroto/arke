package org.tinnwork.arke.glossary;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import static com.vaadin.ui.UI.getCurrent;

import java.io.Serializable;

import org.tinnwork.arke.MainUI;
import org.tinnwork.arke.common.MainWindow;

/**
 * Created by fran on 16/06/17.
 */
public class GlossaryView extends MainWindow implements Serializable {


	Grid<Glossary> terms = new Grid<>();
    TextField filter = new TextField();
    Button newTerm = new Button("Añadir término");
    GlossaryForm glossaryForm = new GlossaryForm();

    public GlossaryView() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        //Listeners
        newTerm.addClickListener(e -> {
            glossaryForm.newGlossaryTerm();
            terms.deselectAll();
        });
        terms.addItemClickListener(event -> glossaryForm.edit(event.getItem()));
        filter.setPlaceholder("Buscar por palabras...");
        filter.addValueChangeListener(e -> refreshTerms(e.getValue()));
        glossaryForm.save.addClickListener(e -> refreshTerms());
        glossaryForm.delete.addFocusListener(e -> refreshTerms());

        MainUI ui = (MainUI) getCurrent();
        //grid
        try{
            terms.setItems(ui.glossaryService.readFromDB());
        }catch (Exception e){}
        terms.addColumn(Glossary::getTerm).setCaption("Término").setExpandRatio(1);
        terms.addColumn(Glossary::getDefinition).setCaption("Definición").setExpandRatio(10);

        terms.setWidth("100%");
    }

    private void buildLayout() {
        HorizontalLayout actions = new HorizontalLayout(filter, newTerm);

        actions.setMargin(false);
        actions.setWidth("100%");
        filter.setWidth("100%");
        actions.setExpandRatio(filter, 1);

        VerticalLayout left = new VerticalLayout(actions, terms);
        left.setSizeFull();
        left.setMargin(false);
        terms.setSizeFull();
        left.setExpandRatio(terms, 1);

        HorizontalLayout mainLayout = new HorizontalLayout(left, glossaryForm);
        mainLayout.setSizeFull();
        mainLayout.setMargin(false);
        mainLayout.setExpandRatio(left, 1);

        addComponent(mainLayout);
        setComponentAlignment(mainLayout, Alignment.TOP_CENTER);
        setExpandRatio(mainLayout, 15.0f);
    }

    public void refreshTerms() {
        refreshTerms(filter.getValue());
    }

    private void refreshTerms(String stringFilter) {
        MainUI ui = (MainUI) getCurrent();
        terms.setItems(ui.glossaryService.findAll(stringFilter));

        //if the term has been deleted
        try {
            if (ui.glossaryService.findAll("id="+glossaryForm.selectedItem.getId()).isEmpty()) {
                glossaryForm.setVisible(false);
            }
        }catch (Exception e){}
    }
}