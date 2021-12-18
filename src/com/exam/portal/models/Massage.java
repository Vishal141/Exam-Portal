package com.exam.portal.models;



public class Massage {
    private String massageId;
    private  String teamId;
    private  String senderId;
    private  String massage;
    private String date;

    public Massage(){}

    public  Massage(String massageId,String teamId,String senderId,String massage,String date){
        this.massageId=massageId;
        this.teamId=teamId;
        this.senderId=senderId;
        this.massage=massage;
        this.date=date;

    }
    public  void  setMassageId(String massageId){this.massageId=massageId;}

    public  void  setTeamId(String teamId){this.teamId=teamId;}

    public  void  setSenderId(String senderId){this.senderId=senderId;}

    public  void  setMassage(String massage){this.massage=massage;}

    public  void  setDate(String date){this.date=date;}

    public  String getMassageId(){return massageId;}

    public  String getTeamId(){return teamId;}

    public String getSenderId(){return senderId;}

    public  String getMassage(){return massage;}

    public String getDate(){return date;}
}
