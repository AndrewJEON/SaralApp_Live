package com.cgp.saral.model;



import java.io.Serializable;


public class Userdata_Bean implements Serializable{


    private String userId;

    private String userFName;

    private String userLName;

    private String gender;

    private String dob;

    private String contact1;

    private String contact2;

    private String language;

    private String state;
    private String mail;

    private String intrest;
    private String imgurl;



    public String getDistrictId() {
        return DistrictId;
    }

    public Userdata_Bean setDistrictId(String districtId) {
        DistrictId = districtId;
        return this;
    }

    private String DistrictId;

    public String getUserName() {
        return UserName;
    }

    public Userdata_Bean setUserName(String userName) {
        UserName = userName;
        return this;
    }

    private String UserName;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;


    public String getSocial_id() {
        return social_id;
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    private String social_id;



    public String DistrictName;

    public String Address;

    public String getStatus() {
        return Status;
    }

    public Userdata_Bean setStatus(String status) {
        Status = status;
        return this;
    }

    public String Status;

    public String getAddress() {
        return Address;
    }

    public Userdata_Bean setAddress(String address) {
        Address = address;
        return this;
    }

    public String getCity() {
        return City;
    }

    public Userdata_Bean setCity(String city) {
        City = city;
        return this;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public Userdata_Bean setDistrictName(String districtName) {
        DistrictName = districtName;
        return this;
    }

    public String City;


    public String lucky_No;

    public String getLucky_Chart() {
        return lucky_Chart;
    }

    public Userdata_Bean setLucky_Chart(String lucky_Chart) {
        this.lucky_Chart = lucky_Chart;
        return this;
    }

    public String getLucky_No() {
        return lucky_No;
    }

    public Userdata_Bean setLucky_No(String lucky_No) {
        this.lucky_No = lucky_No;
        return this;
    }

    public String lucky_Chart;

    public Userdata_Bean(String userFName,String contact,String pass,String email,String dob,String language,String gender,String state,String intresttr) {
        this.state = state;
        this.language = language;

        this.contact2 = pass;
        this.contact1 = contact;
        this.dob = dob;
        this.gender = gender;
        this.userLName = userLName;
        this.userFName = userFName;
        this.mail = email;
        // this.userId = userId;
        this.intrest=intresttr;
    }
    public String getSocialtype() {
        return socialtype;
    }

    public void setSocialtype(String socialtype) {
        this.socialtype = socialtype;
    }


    private String socialtype;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

       /* @Expose
    private List<Integer> intrests = new ArrayList<Integer>();
*/
    public Userdata_Bean()
    {

    }
    public String getIntrest() {
        return intrest;
    }

    public void setIntrest(String intrest) {
        this.intrest = intrest;
    }
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMail() {
        return mail;
    }


    public void setMail(String email) {
        this.mail = email;
    }

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The userFName
     */
    public String getUserFName() {
        return userFName;
    }

    /**
     *
     * @param userFName
     * The user_f_name
     */
    public void setUserFName(String userFName) {
        this.userFName = userFName;
    }

    /**
     *
     * @return
     * The userLName
     */
    public String getUserLName() {
        return userLName;
    }

    /**
     *
     * @param userLName
     * The user_l_name
     */
    public void setUserLName(String userLName) {
        this.userLName = userLName;
    }

    /**
     *
     * @return
     * The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *
     * @param gender
     * The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     *
     * @return
     * The dob
     */
    public String getDob() {
        return dob;
    }

    /**
     *
     * @param dob
     * The dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     *
     * @return
     * The contact1
     */
    public String getContact1() {
        return contact1;
    }

    /**
     *
     * @param contact1
     * The contact1
     */
    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    /**
     *
     * @return
     * The contact2
     */
    public String getContact2() {
        return contact2;
    }

    /**
     *
     * @param contact2
     * The contact2
     */
    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    /**
     *
     * @return
     * The intrests
     */
   /*public List<Integer> getIntrests() {
        return intrests;
    }

    *//**
     *
     * @param intrests
     * The intrests
     *//*
    public void setIntrests(List<Integer> intrests) {
        this.intrests = intrests;
    }*/

}