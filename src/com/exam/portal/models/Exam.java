package com.exam.portal.models;

import java.util.ArrayList;

public class Exam {
    private String examId;
    private String teamId;
    private String creatorId;
    private String title;
    private String examDate;
    private String time;
    private String duration;
    private int count;
    private double maxScore;

    private ArrayList<Question> questions;

    public Exam(){
        questions = new ArrayList<>();
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExamDate() {
        return examDate;
    }

    public void setExamDate(String examString) {
        this.examDate = examString;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public int getCount(){
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public void addQuestion(Question question){
        question.setQuestionId(questions.size()+"");
        question.setExamId(this.examId);
        questions.add(question);
        for(Option option:question.getOptions()){
            option.setExamId(this.examId);
            option.setQuestionId(question.getQuestionId());
        }
    }

    public int getQuestionCount(){
        return questions.size();
    }

    public Question undoQuestion(){
        if(questions.size()==0)
            return null;
        Question question = questions.get(questions.size()-1);
        questions.remove(question);
        return question;
    }

    public Question getLastQuestion(){
        if(questions.size()==0)
            return null;
        return questions.get(questions.size()-1);
    }
}
