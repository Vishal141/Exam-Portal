package com.exam.portal.models;

//this class contains information about teams and exams created notified before and data is saved in local storage.
public final class GeneralInformation {
    private int teamCount;
    private int examCount;

    private static GeneralInformation generalInformation;

    private GeneralInformation(){}

    public static GeneralInformation getInstance(){
        if(generalInformation==null)
            generalInformation = new GeneralInformation();
        return generalInformation;
    }

    public int getTeamCount() {
        return teamCount;
    }

    public void setTeamCount(int teamCount) {
        this.teamCount = teamCount;
    }

    public int getExamCount() {
        return examCount;
    }

    public void setExamCount(int examCount) {
        this.examCount = examCount;
    }
}
