package com.exam.portal.proctoring;

public class ProctorResult {
    private boolean cheatStatus;
    private String timeAt;
    private int cnt;

    public ProctorResult(){
        cheatStatus = false;
        timeAt = "";
        cnt = 0;
    }

    public void setCheatStatus(int duration){
        if(cnt > (duration/60)*10)
             cheatStatus = true;
    }

    public boolean getCheatStatus(){
        return this.cheatStatus;
    }

    public void addTime(long time){
        timeAt = timeAt + " , " + time;
        cnt++;
    }
}
