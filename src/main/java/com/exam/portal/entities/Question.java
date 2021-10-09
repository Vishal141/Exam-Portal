package com.exam.portal.entities;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Question {
    private String QuestionId;
    private String Question;
    private String Options;
    private boolean IsQuestionImg;
    private boolean IsOptionsImg;
    private String Answer;
    private double Point;
    private double NegPoint;
    private int OptionCount;

    public Question(){
        Question = "";
        Options = "";
        Point = 1.0;
        NegPoint = 0.0;
        IsOptionsImg = false;
        IsQuestionImg = false;
        OptionCount = 0;
    }

    public Question(String questionId,String question,boolean isQuestionImg,
                    boolean isOptionsImg,String answer,double point,double negPoint){
        QuestionId = questionId;
        Question = question;
        IsQuestionImg = isQuestionImg;
        IsOptionsImg = isOptionsImg;
        Answer = answer;
        Point = point;
        NegPoint = negPoint;
        OptionCount = 0;
    }

    public String getQuestionId() {
        return QuestionId;
    }

    public void setQuestionId(String questionId) {
        QuestionId = questionId;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public boolean isQuestionImg() {
        return IsQuestionImg;
    }

    public void setQuestionImg(boolean questionImg) {
        IsQuestionImg = questionImg;
    }

    public boolean isOptionsImg() {
        return IsOptionsImg;
    }

    public void setOptionsImg(boolean optionsImg) {
        IsOptionsImg = optionsImg;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public double getPoint() {
        return Point;
    }

    public void setPoint(double point) {
        Point = point;
    }

    public double getNegPoint() {
        return NegPoint;
    }

    public void setNegPoint(double negPoint) {
        NegPoint = negPoint;
    }

    public void addOption(String option){
        OptionCount++;
        option = "("+OptionCount+")"+" "+option;
        option += "\n";
        Options += option;
    }

    private ArrayList<String> extractOptions(){
        StringTokenizer tokenizer = new StringTokenizer(Options,"\n");
        ArrayList<String> options = new ArrayList<>();
        while(tokenizer.hasMoreTokens())
            options.add(tokenizer.nextToken());
        return options;
    }

    public ArrayList<String> getOptions(){
        return extractOptions();
    }

}
