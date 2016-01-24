package com.cgp.saral.model;

/**
 * Created by WeeSync on 27-10-2015.
 */
public class ChatMessage {
    private long id;
    private boolean isMe;
    private String message;
    //private Long userId;
    private String dateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsme() {
        return isMe;
    }

    public void setMe(boolean isMe) {
        this.isMe = isMe;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



    public String getDate() {
        return dateTime;
    }

    public void setDate(String dateTime) {
        this.dateTime = dateTime;
    }


    private String ChatHistoryId;

    public String getUserName() {
        return UserName;
    }

    public ChatMessage setUserName(String userName) {
        UserName = userName;
        return this;
    }

    public ChatMessage setUserId(String userId) {
        UserId = userId;
        return this;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public ChatMessage setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
        return this;
    }

    public String getChatText() {
        return ChatText;
    }

    public ChatMessage setChatText(String chatText) {
        ChatText = chatText;
        return this;
    }

    public String getChatSessionId() {
        return ChatSessionId;
    }

    public ChatMessage setChatSessionId(String chatSessionId) {
        ChatSessionId = chatSessionId;
        return this;
    }

    public String getChatHistoryId() {
        return ChatHistoryId;
    }

    public ChatMessage setChatHistoryId(String chatHistoryId) {
        ChatHistoryId = chatHistoryId;
        return this;
    }

    private String ChatSessionId;
    private String ChatText;
    private String CreatedDate;

    public String getUserId() {
        return UserId;
    }

    private String UserId;
    private String UserName;
}
