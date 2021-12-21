package com.exam.portal.entities;

import java.sql.Timestamp;

public class Message {
    private String massageId;
    private  String teamId;
    private  String senderId;
    private String senderName;
    private  String massage;
    private Timestamp date;

    public  void  setMassageId(String massageId){this.massageId=massageId;}

    public  void  setTeamId(String teamId){this.teamId=teamId;}

    public  void  setSenderId(String senderId){this.senderId=senderId;}

    public  void  setMassage(String massage){this.massage=massage;}

    public  void  setDate(Timestamp date){this.date=date;}

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public  String getMassageId(){return massageId;}

    public  String getTeamId(){return teamId;}

    public String getSenderId(){return senderId;}

    public  String getMassage(){return massage;}

    public Timestamp getDate(){return date;}

}
