package org.tinnwork.arke.group;

import java.io.Serializable;

import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;


/**
 * Created by fran on 16/06/17.
 */
public class PlayersForm extends FormLayout implements Serializable {

    public Label numberOfPlayers = new Label("0 jugadores");
    Grid<Players> players = new Grid<>();

    public PlayersForm() {
        configureComponents();
        buildLayout();
    }

    private void configureComponents() {
        players.setWidth("100%");
        players.addStyleName("grid-transparent");
        players.addColumn(Players::getPlayerName);
        numberOfPlayers.addStyleName("title");
        numberOfPlayers.setWidth("100%");
    }

    private void buildLayout() {
        setSizeUndefined();
        setMargin(true);
        this.addStyleName("v-scrollable");
        this.setHeight("100%");
        addComponents(numberOfPlayers,players);
    }


}