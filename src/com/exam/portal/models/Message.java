package com.exam.portal.models;



public class Message {
    private String massageId;
    private  String teamId;
    private  String senderId;
    private String senderName;
    private  String message;
    private String date;

    public  void  setMassageId(String massageId){this.massageId=massageId;}

    public  void  setTeamId(String teamId){this.teamId=teamId;}

    public  void  setSenderId(String senderId){this.senderId=senderId;}

    public  void setMessage(String message){this.message = message;}

    public  void  setDate(String date){this.date=date;}

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public  String getMassageId(){return massageId;}

    public  String getTeamId(){return teamId;}

    public String getSenderId(){return senderId;}

    public  String getMessage(){return message;}

    public String getDate(){return date;}
}
