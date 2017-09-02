package org.tinnwork.arke.group;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;

/**
 * Created by fran on 15/06/17.
 */
public class Players implements Serializable, Cloneable {

	private int id;
    private String playerName;

    public Players(int id, String playerName){
        this.id=id;
        this.playerName=playerName;
    }

    public Players(){
        super();
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id=id;
    }

    public String getPlayerName(){
        return playerName;
    }

    public void  setPlayerName(String playerName){
        this.playerName=playerName;
    }


    @Override
    public Players clone() throws CloneNotSupportedException {
        try {
            return (Players) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "Juegador {" + "id=" + id + ", nombre=" + playerName + '}';
    }

}