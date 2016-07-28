package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.models.ModelBean;

public class UserBean extends ModelBean {

    private boolean loggedIn;
    private String name;

    public UserBean() {
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(final boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
