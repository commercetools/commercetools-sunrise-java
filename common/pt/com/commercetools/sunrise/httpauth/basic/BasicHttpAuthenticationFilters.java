package com.commercetools.sunrise.httpauth.basic;

import com.commercetools.sunrise.httpauth.HttpAuthenticationFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class BasicHttpAuthenticationFilters extends DefaultHttpFilters {

    @Inject
    public BasicHttpAuthenticationFilters(final HttpAuthenticationFilter httpAuthenticationFilter) {
        super(httpAuthenticationFilter);
    }
}
