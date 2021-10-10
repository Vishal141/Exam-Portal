package com.exam.portal.controllers;

import com.exam.portal.entities.Student;
import com.exam.portal.entities.Teacher;
import com.exam.portal.interfaces.Authentication;
import com.exam.portal.services.AuthenticationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exam-portal/authentication")
public class AuthenticationController {
    private final Authentication authentication;

    public AuthenticationController(){
        authentication = new AuthenticationService();
    }

    @RequestMapping("/student/signUp")
    public String studentRegister(@RequestBody Student student){
        if(authentication.register(student))
            return "SUCCESSFUL";
        return "FAILED";
    }

    @RequestMapping("/student/signIn")
    public String studentLogin(@RequestBody Student student){
        if(authentication.login(student))
            return "SUCCESSFUL";
        return "FAILED";
    }

    @RequestMapping("/teacher/signUp")
    public String teacherRegister(@RequestBody Teacher teacher){
        if(authentication.register(teacher))
            return "SUCCESSFUL";
        return "FAILED";
    }

    @RequestMapping("/teacher/signIn")
    public String teacherLogin(@RequestBody Teacher teacher){
        if(authentication.login(teacher))
            return "SUCCESSFUL";
        return "FAILED";
    }
}
