package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.models.ViewModel;

public class UserBean extends ViewModel {

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
