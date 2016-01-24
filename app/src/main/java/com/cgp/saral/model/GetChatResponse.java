package com.cgp.saral.model;

/**
 * Created by WeeSync on 23/10/15.
 */
public class GetChatResponse {

    public com.cgp.saral.model.GetChatHistoryBySessionIdResult getGetChatHistoryBySessionIdResult() {
        return GetChatHistoryBySessionIdResult;
    }

    public GetChatResponse setGetChatHistoryBySessionIdResult(com.cgp.saral.model.GetChatHistoryBySessionIdResult getChatHistoryBySessionIdResult) {
        GetChatHistoryBySessionIdResult = getChatHistoryBySessionIdResult;
        return this;
    }

    GetChatHistoryBySessionIdResult GetChatHistoryBySessionIdResult;
}
