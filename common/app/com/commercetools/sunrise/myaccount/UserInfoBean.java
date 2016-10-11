package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.models.ViewModel;

public class UserInfoBean extends ViewModel {

    private boolean loggedIn;
    private String name;
    private String email;

    public UserInfoBean() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
