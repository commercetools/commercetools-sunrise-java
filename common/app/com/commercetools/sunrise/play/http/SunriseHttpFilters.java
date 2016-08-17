package com.commercetools.sunrise.play.http;

import com.commercetools.sunrise.common.basicauth.BasicAuthFilter;
import play.filters.csrf.CSRFFilter;
import play.http.HttpFilters;
import play.mvc.EssentialFilter;

import javax.inject.Inject;

/**
 * Based on play.mvc.http.DefaultHttpFilters with fix https://github.com/playframework/playframework/pull/6238, which will be released with Play 2.5.5.
 * REMOVE AS SOON AS IT IS RELEASED!
 */
public class SunriseHttpFilters implements HttpFilters {

    private final EssentialFilter[] filters;

    @Inject
    public SunriseHttpFilters(final BasicAuthFilter basicAuthFilter, final CSRFFilter csrfFilter) {
        this.filters = new EssentialFilter[] {basicAuthFilter.asJava(), csrfFilter.asJava()};
    }

    @Override
    public EssentialFilter[] filters() {
        return filters;
    }
}