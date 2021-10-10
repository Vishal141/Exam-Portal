package com.exam.portal.interfaces;

import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;

public interface Authentication {
    public boolean register(Student student);
    public boolean register(Teacher teacher);
    public boolean login(Student student);
    public boolean login(Teacher teacher);
}
