package com.exam.portal.entities;

import javax.persistence.Transient;
import java.io.File;
import java.util.ArrayList;;

public class Question {
    private String examId;
    private String questionId;
    private String question;
    private boolean isImage;
    private String file;

    private double point;
    private double negPoint;

    private ArrayList<Option> options;
    private String ansIndices;

    public Question(){
        point = 1.0;
        negPoint = 0.0;
        options = new ArrayList<>();
    }

    public Question(String question){
        this();
        this.question = question;
        this.isImage = false;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setIsImage(boolean image) {
        isImage = image;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public double getPoint() {
        return point;
    }

    public void setPoint(double point) {
        this.point = point;
    }

    public double getNegPoint() {
        return negPoint;
    }

    public void setNegPoint(double negPoint) {
        this.negPoint = negPoint;
    }

    public String getAnsIndices() {
        return ansIndices;
    }

    public void setAnsIndices(String ansIndices) {
        this.ansIndices = ansIndices;
    }

    public void addOption(Option option){
        option.setIndex(options.size()+"");
        options.add(option);
    }

    public Option undoOption(){
        int idx = options.size();
        if(idx==0)
            return null;
        Option res = options.get(idx-1);
        options.remove(idx-1);
        return res;
    }

    public void setOptions(ArrayList<Option> options){
        this.options = options;
    }

    public ArrayList<Option> getOptions(){
        return options;
    }

    public int getOptionCount(){
        return options.size();
    }

    public Option getLastQuestion(){
        return options.get(options.size()-1);
    }
}