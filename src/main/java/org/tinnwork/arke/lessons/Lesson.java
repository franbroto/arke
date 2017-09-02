package org.tinnwork.arke.lessons;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;

/**
 * Created by fran on 15/06/17.
 */
public class Lesson implements Serializable, Cloneable {

    private Long id;
    private String lesson;
    private Long idSubject;
    private int numberOfQuestions;

    public Lesson(Long id, String lesson, Long idSubject, int numberOfQuestions){
        this.id=id;
        this.lesson=lesson;
        this.idSubject=idSubject;
        this.numberOfQuestions=numberOfQuestions;
    }
    public Lesson(){
        super();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getLesson(){
        return lesson;
    }

    public void setLesson(String lesson){
        this.lesson=lesson;
    }

    public Long getIdSubject(){
        return idSubject;
    }

    public void  setIdSubject(Long idSubject){
        this.idSubject=idSubject;
    }

    public int getNumberOfQuestions(){
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions){
        this.numberOfQuestions=numberOfQuestions;
    }

    @Override
    public Lesson clone() throws CloneNotSupportedException {
        try {
            return (Lesson) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "Tema {" + "id=" + id + ", tema=" + lesson
                + ", id asignatura=" + idSubject + ", numero de preguntas=" + numberOfQuestions + '}';
    }

}