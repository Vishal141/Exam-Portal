package com.exam.portal.entities;

import java.util.Date;

public class Exam {
    private String ExamId;
    private String TeamId;
    private String CreatorId;
    private Date ExamDate;
    private int Duration;

    public Exam(){}

    public Exam(String examId, String teamId, String creatorId, Date examDate, int duration) {
        ExamId = examId;
        TeamId = teamId;
        CreatorId = creatorId;
        ExamDate = examDate;
        Duration = duration;
    }

    public String getExamId() {
        return ExamId;
    }

    public void setExamId(String examId) {
        ExamId = examId;
    }

    public String getTeamId() {
        return TeamId;
    }

    public void setTeamId(String teamId) {
        TeamId = teamId;
    }

    public String getCreatorId() {
        return CreatorId;
    }

    public void setCreatorId(String creatorId) {
        CreatorId = creatorId;
    }

    public Date getExamDate() {
        return ExamDate;
    }

    public void setExamDate(Date examDate) {
        ExamDate = examDate;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }
}
