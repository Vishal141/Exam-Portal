package com.exam.portal.interfaces;

import com.exam.portal.entities.Exam;
import com.exam.portal.entities.Question;

import java.util.ArrayList;

public interface ExamUtils {
    public boolean createExam(Exam exam,ArrayList<Question> questions);
    public ArrayList<Exam> getExamsScheduledBy(String teacherId);
    public ArrayList<Exam> getExamsScheduledFor(String studentId);
    public Exam getExamById(String examId);
}
