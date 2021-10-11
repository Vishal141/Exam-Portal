package com.exam.portal.interfaces;

import com.exam.portal.entities.BelongTo;
import com.exam.portal.entities.Student;
import com.exam.portal.entities.Team;

public interface TeamUtils {
    public boolean createTeam(Team team);
    public boolean addStudent(BelongTo belongTo);
}
