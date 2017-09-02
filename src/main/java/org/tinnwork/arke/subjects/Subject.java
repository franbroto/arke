package org.tinnwork.arke.subjects;

import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;

/**
 * Created by fran on 15/06/17.
 */
public class Subject implements Serializable, Cloneable {

    private Long id;
    private String subject;
    private int course;
    private int numberOfQuestions;

    public Subject(Long id,String subject, int course, int numberOfQuestions){
        this.id=id;
        this.subject=subject;
        this.course=course;
        this.numberOfQuestions=numberOfQuestions;
    }
    public Subject(){
        super();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getSubject(){
        return subject;
    }

    public void  setSubject(String subject){
        this.subject=subject;
    }

    public int getCourse(){
        return course;
    }

    public void setCourse(int course){
        this.course=course;
    }

    public int getNumberOfQuestions(){
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions){
        this.numberOfQuestions=numberOfQuestions;
    }

    @Override
    public Subject clone() throws CloneNotSupportedException {
        try {
            return (Subject) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "Asignatura {" + "id=" + id + ", asignatura=" + subject
                + ", curso=" + course + ", numero de preguntas=" + numberOfQuestions + '}';
    }

}