package com.exam.portal.entities;

public class Student {
    private String StudentId;
    private String Name;
    private String Email;
    private String ContactNo;
    private String Password;

    public Student(){}

    public Student(String studentId, String name, String email, String contactNo, String password) {
        StudentId = studentId;
        Name = name;
        Email = email;
        ContactNo = contactNo;
        Password = password;
    }

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
