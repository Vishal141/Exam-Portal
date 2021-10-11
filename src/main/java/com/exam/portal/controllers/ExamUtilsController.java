package com.exam.portal.controllers;

import com.exam.portal.entities.Exam;
import com.exam.portal.entities.Question;
import com.exam.portal.interfaces.ExamUtils;
import com.exam.portal.services.ExamUtilsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/exam-portal/exam-utilities")
public class ExamUtilsController{
    private final ExamUtils examUtils;

    public ExamUtilsController(){
        examUtils = new ExamUtilsService();
    }

    @RequestMapping("/create-exam")
    public String createExam(@RequestBody Exam exam, @RequestBody ArrayList<Question> questions){
        if(examUtils.createExam(exam,questions))
            return "SUCCESSFUL";
        return "FAILED";
    }
}
