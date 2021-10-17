package com.exam.portal.controllers;

import com.exam.portal.entities.Exam;
import com.exam.portal.entities.Question;
import com.exam.portal.interfaces.ExamUtils;
import com.exam.portal.proctor.FaceDetector;
import com.exam.portal.services.ExamUtilsService;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.transaction.ReactiveTransaction;
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

    @RequestMapping("/check-proctor")
    public String checkProctor(){
        FaceDetector detector = new FaceDetector();
        if(detector.checkProctor())
            return SUCCESSFUL;
        return FAILED;
    }

    @RequestMapping("/detect-face/{image}")
    public int detectFace(@PathVariable String image){
        FaceDetector detector = new FaceDetector();
        byte[] bytes = Base64.getDecoder().decode(image);
        return detector.detectFace(bytes);
    }

    @RequestMapping("/scheduled-by/{teacherId}")
    public ArrayList<Exam> getExamsScheduledBy(@PathVariable String teacherId){
        return examUtils.getExamsScheduledBy(teacherId);
    }

    @RequestMapping("/get-exam/id={examId}")
    public Exam getExamById(@PathVariable String examId){
        System.out.println(examId);
        return examUtils.getExamById(examId);
    }
}
