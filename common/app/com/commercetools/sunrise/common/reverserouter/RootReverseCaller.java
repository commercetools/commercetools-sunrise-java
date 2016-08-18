package com.commercetools.sunrise.common.reverserouter;

import play.mvc.Call;

public final class RootReverseCaller implements ReverseCaller {
    private RootReverseCaller() {
    }

    private static final Call root = new Call() {
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
    public static final ReverseCaller INSTANCE = new RootReverseCaller();

    @Override
    public Call call(final Object... args) {
        return root;
    }
}
