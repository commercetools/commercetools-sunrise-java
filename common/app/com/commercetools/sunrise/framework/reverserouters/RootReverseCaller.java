package com.commercetools.sunrise.framework.reverserouters;

import play.mvc.Call;

public final class RootReverseCaller implements ReverseCaller {

    private RootReverseCaller() {
    }

    public static final ReverseCaller INSTANCE = new RootReverseCaller();

    private static final Call ROOT = new Call() {
        @Override
        public String url() {
            return "/";
        }

        @Override
        public String method() {
            return "GET";
        }

        @Override
        public String fragment() {
            return null;
        }
    };

    @Override
    public Call call(final Object... args) {
        return ROOT;
    }
}
