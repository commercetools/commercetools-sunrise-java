package com.commercetools.sunrise.common.cache;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.HeaderNames;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public class NoCacheAction extends Action<NoCache> {

    @Override
    public CompletionStage<Result> call(final Http.Context ctx) {
        ctx.response().setHeader(HeaderNames.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        ctx.response().setHeader(HeaderNames.PRAGMA, "no-cache");
        ctx.response().setHeader(HeaderNames.EXPIRES, "0");
        return delegate.call(ctx);
    }
}
