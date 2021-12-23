package com.exam.portal.controllers;

import com.exam.portal.entities.Exam;
import com.exam.portal.entities.ExamResponse;
import com.exam.portal.entities.ExamUpdate;
import com.exam.portal.entities.Image;
import com.exam.portal.interfaces.ExamUtils;
import com.exam.portal.proctor.Proctor;
import com.exam.portal.services.ExamUtilsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Base64;

@RestController
@RequestMapping("/exam-portal/exam-utilities")
public class ExamUtilsController{
    private static final String SUCCESSFUL = "SUCCESSFUL";
    private static final String FAILED = "FAILED";

    private final ExamUtils examUtils;

    public ExamUtilsController(){
        examUtils = new ExamUtilsService();
    }

    @RequestMapping("/create-exam")
    public String createExam(@RequestBody Exam exam){
        if(examUtils.createExam(exam,exam.getQuestions()))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/proctor/get")
    public String getProctorResult(@RequestBody Image image){
        Proctor proctor = new Proctor();
        if(proctor.getResult(image))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/scheduled-by/{teacherId}")
    public ArrayList<Exam> getExamsScheduledBy(@PathVariable String teacherId){
        return examUtils.getExamsScheduledBy(teacherId);
    }

    @RequestMapping("/scheduled-for/{studentId}")
    public ArrayList<Exam> getExamScheduledFor(@PathVariable String studentId){
        return examUtils.getExamsScheduledFor(studentId);
    }

    @RequestMapping("/get-exam/id={examId}")
    public Exam getExamById(@PathVariable String examId){
        examId = "Exam#"+examId;
        Exam exam =  examUtils.getExamById(examId);
       // System.out.println(exam.getQuestions().get(0).isImage());
        return exam;
    }

    @RequestMapping("/student/submit-test")
    public String submitExam(@RequestBody ExamResponse response){
        if(examUtils.submitExam(response))
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/get/submission/{examId}/{studentId}")
    public ExamResponse getStudentSubmission(@PathVariable String examId, @PathVariable String studentId){
        examId = "Exam#"+examId;
        return examUtils.getStudentSubmission(examId,studentId);
    }

    @RequestMapping("/get/update")
    public ExamUpdate checkExamUpdate(@RequestBody ExamUpdate update){
        if(update.getType().equals("create"))
            return examUtils.checkExamUpdate(update);
        return examUtils.checkExamStartUpdate(update);
    }
}
