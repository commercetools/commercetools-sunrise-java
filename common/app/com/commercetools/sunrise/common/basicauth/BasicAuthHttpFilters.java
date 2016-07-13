package com.commercetools.sunrise.common.basicauth;

import play.http.HttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;

/**
 * Based on play.mvc.http.DefaultHttpFilters with fix https://github.com/playframework/playframework/pull/6238, which will be released with Play 2.5.5.
 * REMOVE AS SOON AS IT IS RELEASED!
 */
public class BasicAuthHttpFilters implements HttpFilters {

    private final EssentialFilter[] filters;

    @Inject
    public BasicAuthHttpFilters(final BasicAuthFilter basicAuthFilter) {
        this.filters = new EssentialFilter[] {basicAuthFilter.asJava()};
    }

    @Override
    public EssentialFilter[] filters() {
        return filters;
    }
}