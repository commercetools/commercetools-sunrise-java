package com.commercetools.sunrise.test;

import play.mvc.Call;

public class TestableCall extends Call {

    private final String url;

    public TestableCall(final String url) {
        this.url = url;
    }

    @Override
    public String url() {
        return url;
    }

    @Override
    public String method() {
        return "GET";
    }

    @Override
    public String fragment() {
        return null;
    }
}
