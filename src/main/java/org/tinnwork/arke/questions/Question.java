package org.tinnwork.arke.questions;



import org.apache.commons.beanutils.BeanUtils;

import java.io.Serializable;

/**
 * Created by fran on 15/06/17.
 */
public class Question implements Serializable, Cloneable {

    private Long id;
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private Boolean vAnswer1;
    private Boolean vAnswer2;
    private Boolean vAnswer3;
    private Boolean vAnswer4;
    private Boolean vPir;
    private String exposition;
    private Long idLesson;

    public Question(Long id,
                    String question,
                    String answer1,
                    String answer2,
                    String answer3,
                    String answer4,
                    Boolean vAnswer1,
                    Boolean vAnswer2,
                    Boolean vAnswer3,
                    Boolean vAnswer4,
                    Boolean vPir,
                    String exposition,
                    Long idLesson){
        this.id=id;
        this.question=question;
        this.answer1=answer1;
        this.answer2=answer2;
        this.answer3=answer3;
        this.answer4=answer4;
        this.vAnswer1=vAnswer1;
        this.vAnswer2=vAnswer2;
        this.vAnswer3=vAnswer3;
        this.vAnswer4=vAnswer4;
        this.vPir=vPir;
        this.exposition=exposition;
        this.idLesson=idLesson;
    }

    public Question(){
        super();
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

    public String getQuestion(){
        return question;
    }

    public void setQuestion(String question){
        this.question=question;
    }

    public String getAnswer1(){
        return answer1;
    }

    public void setAnswer1(String answer1){
        this.answer1=answer1;
    }

    public String getAnswer2(){
        return answer2;
    }

    public void setAnswer2(String answer2){
        this.answer2=answer2;
    }

    public String getAnswer3(){
        return answer3;
    }

    public void setAnswer3(String answer3){
        this.answer3=answer3;
    }

    public String getAnswer4(){
        return answer4;
    }

    public void setAnswer4(String answer4){
        this.answer4=answer4;
    }

    public Boolean getVAnswer1(){
        return vAnswer1;
    }

    public void setVAnswer1(Boolean vAnswer1){
        this.vAnswer1=vAnswer1;
    }

    public Boolean getVAnswer2(){
        return vAnswer2;
    }

    public void setVAnswer2(Boolean vAnswer2){
        this.vAnswer2=vAnswer2;
    }

    public Boolean getVAnswer3(){
        return vAnswer3;
    }

    public void setVAnswer3(Boolean vAnswer3){
        this.vAnswer3=vAnswer3;
    }

    public Boolean getVAnswer4(){
        return vAnswer4;
    }

    public void setVAnswer4(Boolean vAnswer4){
        this.vAnswer4=vAnswer4;
    }

    public Boolean getVPir(){
        return vPir;
    }

    public void setVPir(Boolean vPir){
        this.vPir=vPir;
    }

    public String getExposition(){
        return exposition;
    }

    public void setExposition(String exposition){
        this.exposition=exposition;
    }

    public Long getIdLesson(){
        return idLesson;
    }

    public void  setIdLesson(Long idLesson){
        this.idLesson=idLesson;
    }

    @Override
    public Question clone() throws CloneNotSupportedException {
        try {
            return (Question) BeanUtils.cloneBean(this);
        } catch (Exception ex) {
            throw new CloneNotSupportedException();
        }
    }

    @Override
    public String toString() {
        return "Pregunta {" + "id=" + id + ", pregunta=" + question +
                ", pregunta pir =" + vPir +
                ", respuesta 1=" + answer1 + ", verdadera="+ vAnswer1 +
                ", respuesta 2=" + answer2 + ", verdadera="+ vAnswer2 +
                ", respuesta 3=" + answer3 + ", verdadera="+ vAnswer3 +
                ", respuesta 4=" + answer4 + ", verdadera="+ vAnswer4 +
                ", id tema=" + idLesson + '}';
    }

}