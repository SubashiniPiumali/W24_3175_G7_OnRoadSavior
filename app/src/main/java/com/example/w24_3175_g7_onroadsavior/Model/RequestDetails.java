package com.example.w24_3175_g7_onroadsavior.Model;

public class RequestDetails {
    private String breakDownRequestId;
    private String breakDownType;
    private String location;
    private String description;
    private String createdDate;
    private String updateDate;
    private String image;
    private String userId;
    private String providerId;

    private String userName;

    private String status;
    private String phoneNo;

    private String imageUrl;

    public RequestDetails(){}
    public RequestDetails(String breakDownType, String location, String description, String createdDate, String updateDate, String image,
                          String userId, String providerId, String userName, String phoneNo, String breakDownRequestId, String status, String imageUrl) {
        this.breakDownType = breakDownType;
        this.location = location;
        this.description = description;
        this.createdDate = createdDate;
        this.updateDate = updateDate;
        this.image = image;
        this.userId = userId;
        this.providerId = providerId;
        this.userName = userName;
        this.phoneNo = phoneNo;
        this.breakDownRequestId = breakDownRequestId;
        this.status=status;
        this.imageUrl = imageUrl;
    }

    public String getBreakDownRequestId() {
        return breakDownRequestId;
    }

    public void setBreakDownRequestId(String breakDownRequestId) {
        this.breakDownRequestId = breakDownRequestId;
    }

    public String getBreakDownType() {
        return breakDownType;
    }

    public void setBreakDownType(String breakDownType) {
        this.breakDownType = breakDownType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
