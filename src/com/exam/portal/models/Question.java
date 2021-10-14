package com.exam.portal.models;

import java.io.File;
import java.util.ArrayList;;

public class Question {
    private String questionId;
    private String question;
    private boolean isImage;
    private File file;

    private double point;
    private double negPoint;

    private ArrayList<Option> options;
    private String ansIndices;

    public Question(){
        point = 1.0;
        negPoint = 0.0;
    }

    public Question(String question){
        this();
        this.question = question;
        this.isImage = false;
    }

    public Question(File file){
        this();
        this.isImage = true;
        this.file = file;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
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

    public void setImage(boolean image) {
        isImage = image;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
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

    public void addOption(Option option){
        option.setIndex(options.size());
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

    public Option getLatestOptionAdded(){
        return options.get(options.size()-1);
    }
}
