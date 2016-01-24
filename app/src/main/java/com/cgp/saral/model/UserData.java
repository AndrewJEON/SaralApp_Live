package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 01/10/15.
 */
public class UserData implements Serializable {

    String UserId;
    String UserName;
    String FirstName;
    String LastName;
    String DOB;
    String MobileNumber;
    String Language;
    String Gender;
    String State;
    String City;
    String DistrictName;
    String Address;
    String Email;
    String CreatedDate;
    String Password;
    String FacebookId;
    String GoogleId;
    String InterestsTag;
    String StateName;

    String LuckyChart;

    public String getDistrictId() {
        return DistrictId;
    }

    public UserData setDistrictId(String districtId) {
        DistrictId = districtId;
        return this;
    }

    private String DistrictId;

    public String getPhotoPath() {
        return PhotoPath;
    }

    public UserData setPhotoPath(String photoPath) {
        PhotoPath = photoPath;
        return this;
    }

    String PhotoPath;

    public String getLuckyNumber() {
        return LuckyNumber;
    }

    public UserData setLuckyNumber(String luckyNumber) {
        LuckyNumber = luckyNumber;
        return this;
    }

    public String getLuckyChart() {
        return LuckyChart;
    }

    public UserData setLuckyChart(String luckyChart) {
        LuckyChart = luckyChart;
        return this;
    }

    String LuckyNumber;

    public String getAddress() {
        return Address;
    }

    public UserData setAddress(String address) {
        Address = address;
        return this;
    }

    public String getCity() {
        return City;
    }

    public UserData setCity(String city) {
        City = city;
        return this;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public UserData setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
        return this;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public UserData setDistrictName(String districtName) {
        DistrictName = districtName;
        return this;
    }

    public String getDOB() {
        return DOB;
    }

    public UserData setDOB(String DOB) {
        this.DOB = DOB;
        return this;
    }

    public String getEmail() {
        return Email;
    }

    public UserData setEmail(String email) {
        Email = email;
        return this;
    }

    public String getFacebookId() {
        return FacebookId;
    }

    public UserData setFacebookId(String facebookId) {
        FacebookId = facebookId;
        return this;
    }

    public String getFirstName() {
        return FirstName;
    }

    public UserData setFirstName(String firstName) {
        FirstName = firstName;
        return this;
    }

    public String getGender() {
        return Gender;
    }

    public UserData setGender(String gender) {
        Gender = gender;
        return this;
    }

    public String getGoogleId() {
        return GoogleId;
    }

    public UserData setGoogleId(String googleId) {
        GoogleId = googleId;
        return this;
    }

    public String getInterestsTag() {
        return InterestsTag;
    }

    public UserData setInterestsTag(String interestsTag) {
        InterestsTag = interestsTag;
        return this;
    }

    public String getLanguage() {
        return Language;
    }

    public UserData setLanguage(String language) {
        Language = language;
        return this;
    }

    public String getLastName() {
        return LastName;
    }

    public UserData setLastName(String lastName) {
        LastName = lastName;
        return this;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public UserData setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
        return this;
    }

    public String getPassword() {
        return Password;
    }

    public UserData setPassword(String password) {
        Password = password;
        return this;
    }

    public String getState() {
        return State;
    }

    public UserData setState(String state) {
        State = state;
        return this;
    }

    public String getStateName() {
        return StateName;
    }

    public UserData setStateName(String stateName) {
        StateName = stateName;
        return this;
    }

    public String getUserId() {
        return UserId;
    }

    public UserData setUserId(String userId) {
        UserId = userId;
        return this;
    }

    public String getUserName() {
        return UserName;
    }

    public UserData setUserName(String userName) {
        UserName = userName;
        return this;
    }


}
