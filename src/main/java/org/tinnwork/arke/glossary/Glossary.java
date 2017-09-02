package org.tinnwork.arke.glossary;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;

/**
 * Created by fran on 15/06/17.
 */
public class Glossary implements Serializable, Cloneable {

	private Long id;
    private String term;
    private String definition;

    public Glossary(Long id, String term, String definition){
        this.id=id;
        this.term=term;
        this.definition=definition;
    }

    public Glossary(){
        super();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getTerm(){
        return term;
    }

    public void  setTerm(String term){
        this.term=term;
    }

    public String getDefinition(){
        return definition;
    }

    public void setDefinition(String definition){
        this.definition=definition;
    }

    @Override
    public Glossary clone() throws CloneNotSupportedException {
        try {
            return (Glossary) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "Glosario {" + "id=" + id + ", termino=" + term
                + ", definicion=" + definition + '}';
    }

}