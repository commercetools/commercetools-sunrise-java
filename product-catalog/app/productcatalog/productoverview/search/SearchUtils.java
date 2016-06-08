package productcatalog.productoverview.search;

import common.contexts.RequestContext;

import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;

public final class SearchUtils {

    private SearchUtils() {
    }

    public static String localizeExpression(final String expr, final Locale locale) {
        return expr.replaceAll("\\{\\{locale\\}\\}", locale.toLanguageTag());
    }

    public static List<String> selectedValues(final String key, final RequestContext requestContext) {
        return requestContext.getQueryString().getOrDefault(key, emptyList());
    }
}
