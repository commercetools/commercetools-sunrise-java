package common.actions;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.HeaderNames;
import play.mvc.Result;

public class NoCacheAction extends Action<NoCache> {

    @Override
    public F.Promise<Result> call(final Http.Context ctx) throws Throwable {
        ctx.response().setHeader(HeaderNames.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        ctx.response().setHeader(HeaderNames.PRAGMA, "no-cache");
        ctx.response().setHeader(HeaderNames.EXPIRES, "0");
        return delegate.call(ctx);
    }
}
