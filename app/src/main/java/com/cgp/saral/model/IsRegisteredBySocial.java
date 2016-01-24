package com.cgp.saral.model;

import java.io.Serializable;

/**
 * Created by WeeSync on 17/10/15.
 */
public class IsRegisteredBySocial implements Serializable {

    public com.cgp.saral.model.IsRegisteredBySocialResult getIsRegisteredBySocialResult() {
        return IsRegisteredBySocialResult;
    }

    public IsRegisteredBySocial setIsRegisteredBySocialResult(com.cgp.saral.model.IsRegisteredBySocialResult isRegisteredBySocialResult) {
        IsRegisteredBySocialResult = isRegisteredBySocialResult;
        return this;
    }

    IsRegisteredBySocialResult IsRegisteredBySocialResult;
}
