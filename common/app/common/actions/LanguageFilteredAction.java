package common.actions;

import play.Play;
import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;

import static java.util.Collections.emptyList;


public class LanguageFilteredAction extends Action<LanguageFiltered> {
    private static final String LANGS = "play.i18n.langs";

    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        final List<String> allowedLanguages = Play.application().configuration().getStringList(LANGS, emptyList());
        final String path = ctx.request()._underlyingRequest().path();
        final String languageTag = getfirstParamFromPath(path);

        if(allowedLanguages.contains(languageTag)) {
            return delegate.call(ctx);
        } else {
            return F.Promise.pure(redirect("/"));
        }
    }

    private String getfirstParamFromPath(final String path) {
        final String[] params = path.substring(1).split("/");
        if(params.length > 0) {
            return params[0];
        } else {
            return "";
        }
    }
}