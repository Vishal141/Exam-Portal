package com.exam.portal.entities;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Question {
    private String questionId;
    private String question;
    private String options;
    private boolean isQuestionImg;
    private boolean isOptionsImg;
    private String answer;
    private double point;
    private double negPoint;
    private int optionCount;

    public Question(){
        question = "";
        options = "";
        point = 1.0;
        negPoint = 0.0;
        isOptionsImg = false;
        isQuestionImg = false;
        optionCount = 0;
    }

    public Question(String questionId, String question, boolean isQuestionImg, boolean isOptionsImg,
                    double point, double negPoint) {
        this.questionId = questionId;
        this.question = question;
        this.isQuestionImg = isQuestionImg;
        this.isOptionsImg = isOptionsImg;
        this.point = point;
        this.negPoint = negPoint;
        optionCount = 0;
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

    public boolean isQuestionImg() {
        return isQuestionImg;
    }

    public void setQuestionImg(boolean questionImg) {
        isQuestionImg = questionImg;
    }

    public boolean isOptionsImg() {
        return isOptionsImg;
    }

    public void setOptionsImg(boolean optionsImg) {
        isOptionsImg = optionsImg;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public void addOption(String option){
        optionCount++;
        option = "("+optionCount+")"+" "+option;
        option += "\n";
        options += option;
    }

    private ArrayList<String> extractOptions(){
        StringTokenizer tokenizer = new StringTokenizer(options,"\n");
        ArrayList<String> options = new ArrayList<>();
        while(tokenizer.hasMoreTokens())
            options.add(tokenizer.nextToken());
        return options;
    }

    public ArrayList<String> getOptions(){
        return extractOptions();
    }

}
