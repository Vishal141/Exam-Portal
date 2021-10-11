package com.exam.portal.entities;

import java.util.Date;

public class Team {
    private String TeamId;
    private String Name;
    private String CreatorId;
    private Date DateCreated;

    public Team(){}

    public Team(String teamId, String name, String creatorId, Date dateCreated) {
        TeamId = teamId;
        Name = name;
        CreatorId = creatorId;
        DateCreated = dateCreated;
    }

    public String getTeamId() {
        return TeamId;
    }

    public void setTeamId(String teamId) {
        TeamId = teamId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCreatorId() {
        return CreatorId;
    }

    public void setCreatorId(String creatorId) {
        CreatorId = creatorId;
    }

    public Date getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        DateCreated = dateCreated;
    }
}
