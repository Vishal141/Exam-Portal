package com.exam.portal.models;

public class QuestionResponse {
    private String QId;
    private String ResponseType;
    private String response;

    public String getQId() {
        return QId;
    }

    public void setQId(String QId) {
        this.QId = QId;
    }

    public String getResponseType() {
        return ResponseType;
    }

    public void setResponseType(String responseType) {
        ResponseType = responseType;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
