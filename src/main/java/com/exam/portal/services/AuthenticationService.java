package com.exam.portal.services;

import com.exam.portal.database.AuthenticationDb;
import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;
import com.exam.portal.interfaces.Authentication;

public class AuthenticationService implements Authentication {
    private AuthenticationDb authentication;

    public AuthenticationService(){
        authentication = new AuthenticationDb();
    }

    @Override
    public boolean register(Student student) {
        return authentication.register(student);
    }

    @Override
    public boolean register(Teacher teacher) {
        return authentication.register(teacher);
    }

    @Override
    public boolean login(Student student) {
        return authentication.login(student);
    }

    @Override
    public boolean login(Teacher teacher) {
        return authentication.login(teacher);
    }
}
