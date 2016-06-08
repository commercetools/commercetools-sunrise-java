package common.hooks;

import play.mvc.Http;

import java.util.concurrent.CompletionStage;

public interface RequestHook extends Hook {
    CompletionStage<?> onRequest(final Http.Context context);
}
