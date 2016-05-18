package common.inject;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.controllers.SunriseController;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.money.CurrencyUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class UserContextProvider implements Provider<UserContext> {
    @Inject
    private Http.Context context;
    @Inject
    private ProjectContext projectContext;

    @Override
    public UserContext get() {
        final String languageTag = getLanguageTag(context);
        final List<Locale> acceptedLocales = SunriseController.acceptedLocales(languageTag, context.request(), projectContext);
        final CountryCode currentCountry = SunriseController.currentCountry(context.session(), projectContext);
        final CurrencyUnit currentCurrency = SunriseController.currentCurrency(currentCountry, projectContext);
        return UserContext.of(acceptedLocales, currentCountry, currentCurrency);
    }

    private String getLanguageTag(final Http.Context context) {
        final int i = indexLanguageTagInRoute(context);
        return context.request().path().split("/")[i];
    }

    private int indexLanguageTagInRoute(final Http.Context context) {
        final String patternString = context.args.get("ROUTE_PATTERN").toString().replaceAll("<[^>]+>", "");//hack since splitting '$languageTag<[^/]+>' with '/' would create more words
        final List<String> strings = Arrays.asList(patternString.split("/"));
        return strings.indexOf("$languageTag");
    }
}
