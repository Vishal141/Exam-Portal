package com.exam.portal.server;

import com.exam.portal.models.*;

public interface Server {
    public boolean login(Student student);
    public boolean login(Teacher teacher);
    public boolean register(Student student);
    public boolean register(Teacher teacher);
    public boolean createTeam(Team team);
    public boolean addStudent(BelongTo belongTo);
    public boolean createExam(Exam exam);
}
