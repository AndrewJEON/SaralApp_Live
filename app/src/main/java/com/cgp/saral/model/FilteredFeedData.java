package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 30/10/15.
 */
public class FilteredFeedData implements Serializable{

    public com.cgp.saral.model.SearchContentResult getSearchContentResult() {
        return SearchContentResult;
    }

    public FilteredFeedData setSearchContentResult(com.cgp.saral.model.SearchContentResult searchContentResult) {
        SearchContentResult = searchContentResult;
        return this;
    }

    SearchContentResult SearchContentResult;
}
