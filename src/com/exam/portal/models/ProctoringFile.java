package com.exam.portal.models;

import com.exam.portal.proctoring.ProctorResult;

public class ProctoringFile {
    private String examId;
    private String studentId;
    private String file;
    private String type;

    private ProctorResult result;

    private ProctorResult proctorResult;

    public ProctoringFile(){}

    public ProctoringFile(String examId, String studentId, String file,String type) {
        this.examId = examId;
        this.studentId = studentId;
        this.file = file;
        this.type = type;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ProctorResult getProctorResult() {
        return proctorResult;
    }

    public void setProctorResult(ProctorResult proctorResult) {
        this.proctorResult = proctorResult;
    }

    public ProctorResult getResult() {
        return result;
    }

    public void setResult(ProctorResult result) {
        this.result = result;
    }
}
