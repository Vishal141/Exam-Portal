package com.exam.portal.interfaces;

import com.exam.portal.entities.Exam;
import com.exam.portal.entities.Question;

import java.util.ArrayList;

public interface ExamUtils {
    public boolean createExam(Exam exam,ArrayList<Question> questions);
}
