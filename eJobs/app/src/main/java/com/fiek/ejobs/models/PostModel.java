package com.fiek.ejobs.models;

import java.util.Date;

public class PostModel
{
    private String JobId;
    private String CompanyName;
    private String Location;
    private String FreeSpots;
    private String Description;
    private String PhotoPath;
    private String Position;
    private Date ExpirationDate;

    public PostModel() {
    }

    public PostModel(String Id,String companyName, String location, String freeSpots, String description,String photoPath,String position, Date expirationDate) {
       JobId=Id;
        CompanyName = companyName;
        Location = location;
        FreeSpots = freeSpots;
        Description = description;
       PhotoPath = photoPath;
        Position = position;
        ExpirationDate = expirationDate;
    }

    public String getJobId() {
        return JobId;
    }

    public void setJobId(String jobId) {
        JobId = jobId;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getFreeSpots() {
        return FreeSpots;
    }

    public void setFreeSpots(String freeSpots) {
        FreeSpots = freeSpots;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPhotoPath() {
        return PhotoPath;
    }

    public void setPhotoPath(String photoPath) {
        PhotoPath = photoPath;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public Date getExpirationDate() {
        return ExpirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        ExpirationDate = expirationDate;
    }
}
