package com.cgp.saral.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WeeSync on 06/10/15.
 */
public class Data implements Serializable{
    private Object Address;
    private String BookingDate;
    private String BookingId;
    private String City;
    private String CreatedBy;
    private String CreatedDate;
    private String DOB;
    private Integer DistrictId;
    private Object DistrictName;
    private String Email;
    private Object FacebookId;
    private String FirstName;
    private Integer Gender;
    private String GenderName;
    private Object GoogleId;
    private String InterestsTag;
    private Integer Language;
    private String LanguageName;
    private String LastName;
    private String LastUpdatedDate;
    private String LuckyChart;
    private String LuckyNumber;
    private String MobileNumber;
    private String Password;
    private String RoleId;
    private String RoleName;

    private String State;
    private String StateName;
    private String Status;
    private String StatusName;
    private String UserId;
    private String UserName;


    public String getPhotoPath() {
        return PhotoPath;
    }

    public Data setPhotoPath(String photoPath) {
        PhotoPath = photoPath;
        return this;
    }

    String PhotoPath;
    /**
     *
     * @return
     * The Address
     */
    public Object getAddress() {
        return Address;
    }

    /**
     *
     * @param Address
     * The Address
     */
    public void setAddress(Object Address) {
        this.Address = Address;
    }

    /**
     *
     * @return
     * The BookingDate
     */
    public String getBookingDate() {
        return BookingDate;
    }

    /**
     *
     * @param BookingDate
     * The BookingDate
     */
    public void setBookingDate(String BookingDate) {
        this.BookingDate = BookingDate;
    }

    /**
     *
     * @return
     * The BookingId
     */
    public String getBookingId() {
        return BookingId;
    }

    /**
     *
     * @param BookingId
     * The BookingId
     */
    public void setBookingId(String BookingId) {
        this.BookingId = BookingId;
    }

    /**
     *
     * @return
     * The City
     */
    public String getCity() {
        return City;
    }

    /**
     *
     * @param City
     * The City
     */
    public void setCity(String City) {
        this.City = City;
    }

    /**
     *
     * @return
     * The CreatedBy
     */
    public String getCreatedBy() {
        return CreatedBy;
    }

    /**
     *
     * @param CreatedBy
     * The CreatedBy
     */
    public void setCreatedBy(String CreatedBy) {
        this.CreatedBy = CreatedBy;
    }

    /**
     *
     * @return
     * The CreatedDate
     */
    public String getCreatedDate() {
        return CreatedDate;
    }

    /**
     *
     * @param CreatedDate
     * The CreatedDate
     */
    public void setCreatedDate(String CreatedDate) {
        this.CreatedDate = CreatedDate;
    }

    /**
     *
     * @return
     * The DOB
     */
    public String getDOB() {
        return DOB;
    }

    /**
     *
     * @param DOB
     * The DOB
     */
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    /**
     *
     * @return
     * The DistrictId
     */
    public Integer getDistrictId() {
        return DistrictId;
    }

    /**
     *
     * @param DistrictId
     * The DistrictId
     */
    public void setDistrictId(Integer DistrictId) {
        this.DistrictId = DistrictId;
    }

    /**
     *
     * @return
     * The DistrictName
     */
    public Object getDistrictName() {
        return DistrictName;
    }

    /**
     *
     * @param DistrictName
     * The DistrictName
     */
    public void setDistrictName(Object DistrictName) {
        this.DistrictName = DistrictName;
    }

    /**
     *
     * @return
     * The Email
     */
    public String getEmail() {
        return Email;
    }

    /**
     *
     * @param Email
     * The Email
     */
    public void setEmail(String Email) {
        this.Email = Email;
    }

    /**
     *
     * @return
     * The FacebookId
     */
    public Object getFacebookId() {
        return FacebookId;
    }

    /**
     *
     * @param FacebookId
     * The FacebookId
     */
    public void setFacebookId(Object FacebookId) {
        this.FacebookId = FacebookId;
    }

    /**
     *
     * @return
     * The FirstName
     */
    public String getFirstName() {
        return FirstName;
    }

    /**
     *
     * @param FirstName
     * The FirstName
     */
    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    /**
     *
     * @return
     * The Gender
     */
    public Integer getGender() {
        return Gender;
    }

    /**
     *
     * @param Gender
     * The Gender
     */
    public void setGender(Integer Gender) {
        this.Gender = Gender;
    }

    /**
     *
     * @return
     * The GenderName
     */
    public String getGenderName() {
        return GenderName;
    }

    /**
     *
     * @param GenderName
     * The GenderName
     */
    public void setGenderName(String GenderName) {
        this.GenderName = GenderName;
    }

    /**
     *
     * @return
     * The GoogleId
     */
    public Object getGoogleId() {
        return GoogleId;
    }

    /**
     *
     * @param GoogleId
     * The GoogleId
     */
    public void setGoogleId(Object GoogleId) {
        this.GoogleId = GoogleId;
    }

    /**
     *
     * @return
     * The InterestsTag
     */
    public String getInterestsTag() {
        return InterestsTag;
    }

    /**
     *
     * @param InterestsTag
     * The InterestsTag
     */
    public void setInterestsTag(String InterestsTag) {
        this.InterestsTag = InterestsTag;
    }

    /**
     *
     * @return
     * The Language
     */
    public Integer getLanguage() {
        return Language;
    }

    /**
     *
     * @param Language
     * The Language
     */
    public void setLanguage(Integer Language) {
        this.Language = Language;
    }

    /**
     *
     * @return
     * The LanguageName
     */
    public String getLanguageName() {
        return LanguageName;
    }

    /**
     *
     * @param LanguageName
     * The LanguageName
     */
    public void setLanguageName(String LanguageName) {
        this.LanguageName = LanguageName;
    }

    /**
     *
     * @return
     * The LastName
     */
    public String getLastName() {
        return LastName;
    }

    /**
     *
     * @param LastName
     * The LastName
     */
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    /**
     *
     * @return
     * The LastUpdatedDate
     */
    public String getLastUpdatedDate() {
        return LastUpdatedDate;
    }

    /**
     *
     * @param LastUpdatedDate
     * The LastUpdatedDate
     */
    public void setLastUpdatedDate(String LastUpdatedDate) {
        this.LastUpdatedDate = LastUpdatedDate;
    }

    /**
     *
     * @return
     * The LuckyChart
     */
    public String getLuckyChart() {
        return LuckyChart;
    }

    /**
     *
     * @param LuckyChart
     * The LuckyChart
     */
    public void setLuckyChart(String LuckyChart) {
        this.LuckyChart = LuckyChart;
    }

    /**
     *
     * @return
     * The LuckyNumber
     */
    public String getLuckyNumber() {
        return LuckyNumber;
    }

    /**
     *
     * @param LuckyNumber
     * The LuckyNumber
     */
    public void setLuckyNumber(String LuckyNumber) {
        this.LuckyNumber = LuckyNumber;
    }

    /**
     *
     * @return
     * The MobileNumber
     */
    public String getMobileNumber() {
        return MobileNumber;
    }

    /**
     *
     * @param MobileNumber
     * The MobileNumber
     */
    public void setMobileNumber(String MobileNumber) {
        this.MobileNumber = MobileNumber;
    }

    /**
     *
     * @return
     * The Password
     */
    public String getPassword() {
        return Password;
    }

    /**
     *
     * @param Password
     * The Password
     */
    public void setPassword(String Password) {
        this.Password = Password;
    }

    /**
     *
     * @return
     * The RoleId
     */
    public String getRoleId() {
        return RoleId;
    }

    /**
     *
     * @param RoleId
     * The RoleId
     */
    public void setRoleId(String RoleId) {
        this.RoleId = RoleId;
    }

    /**
     *
     * @return
     * The RoleName
     */
    public String getRoleName() {
        return RoleName;
    }

    /**
     *
     * @param RoleName
     * The RoleName
     */
    public void setRoleName(String RoleName) {
        this.RoleName = RoleName;
    }



    /**
     *
     * @return
     * The State
     */
    public String getState() {
        return State;
    }

    /**
     *
     * @param State
     * The State
     */
    public void setState(String State) {
        this.State = State;
    }

    /**
     *
     * @return
     * The StateName
     */
    public String getStateName() {
        return StateName;
    }

    /**
     *
     * @param StateName
     * The StateName
     */
    public void setStateName(String StateName) {
        this.StateName = StateName;
    }

    /**
     *
     * @return
     * The Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     *
     * @param Status
     * The Status
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }

    /**
     *
     * @return
     * The StatusName
     */
    public String getStatusName() {
        return StatusName;
    }

    /**
     *
     * @param StatusName
     * The StatusName
     */
    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }

    /**
     *
     * @return
     * The UserId
     */
    public String getUserId() {
        return UserId;
    }

    /**
     *
     * @param UserId
     * The UserId
     */
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    /**
     *
     * @return
     * The UserName
     */
    public String getUserName() {
        return UserName;
    }

    /**
     *
     * @param UserName
     * The UserName
     */
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }



}
