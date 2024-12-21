package com.example.w24_3175_g7_onroadsavior.Model;

public class BreakdownRequestDetails {

    private String createdDate;
    private String updatedDate;
    private String userId;
    private String providerId;
    private String userName;
    private String providerName;
    private String breakdownType;
    private String currentLocation;
    private String description;
    private String imageUrl;
    private String status;
    private String providerLocation;


    public BreakdownRequestDetails(String createdDate, String updatedDate, String userId, String providerId, String userName, String providerName, String breakdownType, String currentLocation, String description, String imageUrl, String status, String providerLocation) {
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.userId = userId;
        this.providerId = providerId;
        this.userName = userName;
        this.providerName = providerName;
        this.breakdownType = breakdownType;
        this.currentLocation = currentLocation;
        this.description = description;
        this.imageUrl = imageUrl;
        this.status = status;
        this.providerLocation = providerLocation;
    }

    public BreakdownRequestDetails() {

    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
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

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getBreakdownType() {
        return breakdownType;
    }

    public void setBreakdownType(String breakdownType) {
        this.breakdownType = breakdownType;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProviderLocation() {
        return providerLocation;
    }

    public void setProviderLocation(String providerLocation) {
        this.providerLocation = providerLocation;
    }
}
