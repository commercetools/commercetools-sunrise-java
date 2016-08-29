package com.commercetools.sunrise.common.httpauth.basic;

import com.commercetools.sunrise.common.httpauth.HttpAuthenticationFilter;
import play.http.DefaultHttpFilters;

import javax.inject.Inject;

public class BasicHttpAuthenticationFilter extends DefaultHttpFilters {

    @Inject
    public BasicHttpAuthenticationFilter(final HttpAuthenticationFilter httpAuthenticationFilter) {
        super(httpAuthenticationFilter);
    }
}
