package com.exam.portal.models;

import java.io.File;

public class ProctoringFile {
    private String examId;
    private String studentId;
    private File file;
    private String type;

    public ProctoringFile(){}

    public ProctoringFile(String examId, String studentId, File file,String type) {
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
