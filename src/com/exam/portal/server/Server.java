package com.exam.portal.server;

import com.exam.portal.models.*;

import java.util.ArrayList;

public interface Server {
    public boolean login(Student student);
    public boolean login(Teacher teacher);
    public boolean register(Student student);
    public boolean register(Teacher teacher);
    public boolean createTeam(Team team);
    public boolean addStudent(BelongTo belongTo);
    public boolean createExam(Exam exam);
    public Teacher getTeacher(String email);
    public Student getStudent(String email);
    public ArrayList<Teacher> searchTeacher(String text);
    public ArrayList<Student> searchStudent(String text);
    public ArrayList<Team> getStudentsTeams(String Id);
    public ArrayList<Team> getTeachersTeams(String Id);
    public boolean updateTeacher(Teacher teacher);
    public boolean checkProctor();
    public int detectFace(String bytes);
    public void sendProctorFile(ProctoringFile file);
    public ArrayList<Exam> getExamScheduledBy(String teacherId);
    public Exam getExamById(String examId);
}
