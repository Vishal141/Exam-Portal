package com.exam.portal.services;

import com.exam.portal.database.ExamUtilsDb;
import com.exam.portal.entities.Exam;
import com.exam.portal.entities.Question;
import com.exam.portal.interfaces.ExamUtils;

import java.util.ArrayList;

public class ExamUtilsService implements ExamUtils {
    private final ExamUtilsDb examUtilsDb;

    public ExamUtilsService(){
        examUtilsDb = new ExamUtilsDb();
    }

    @Override
    public boolean createExam(Exam exam, ArrayList<Question> questions) {
        return examUtilsDb.createExam(exam) && examUtilsDb.addQuestions(questions);
    }

    @Override
    public ArrayList<Exam> getExamsScheduledBy(String teacherId) {
        return examUtilsDb.getExamsScheduledBy(teacherId);
    }

    @Override
    public ArrayList<Exam> getExamsScheduledFor(String studentId) {
        return examUtilsDb.getExamsScheduledFor(studentId);
    }

    @Override
    public Exam getExamById(String examId) {
        return examUtilsDb.getExamById(examId);
    }
}
