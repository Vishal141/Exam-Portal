package com.exam.portal.entities;

public class Teacher {
    private String TeacherId;
    private String Name;
    private String Email;
    private String ContactNo;
    private String Password;

    public Teacher(){}

    public Teacher(String teacherId, String name, String email, String contactNo, String password) {
        TeacherId = teacherId;
        Name = name;
        Email = email;
        ContactNo = contactNo;
        Password = password;
    }

    public String getTeacherId() {
        return TeacherId;
    }

    public void setTeacherId(String teacherId) {
        TeacherId = teacherId;
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
