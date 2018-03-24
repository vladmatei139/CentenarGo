package com.example.ioan_emanuelpopescu.centenargov2.wrappers;

/**
 * Created by Ioan-Emanuel Popescu on 24-Mar-18.
 */

public class Images {
    private Integer uuid;
    private Integer userID;
    private Integer landmarkID;
    private String title;
    private String imgPath;

    public Images() {
    }

    public Images(Integer uuid, Integer userID, Integer landmarkID, String title, String imgPath) {
        this.uuid = uuid;
        this.userID = userID;
        this.landmarkID = landmarkID;
        this.title = title;
        this.imgPath = imgPath;
    }

    public Integer getUuid() {
        return uuid;
    }
    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public Integer getUserID() {
        return userID;
    }
    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getLandmarkID() {
        return landmarkID;
    }
    public void setLandmarkID(Integer landmarkID) {
        this.landmarkID = landmarkID;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgPath() {
        return imgPath;
    }
    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
