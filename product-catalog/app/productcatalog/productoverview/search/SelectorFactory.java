package productcatalog.productoverview.search;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Collections.emptyList;

public abstract class SelectorFactory<T> {

    private final Map<String, List<String>> queryString;

    protected SelectorFactory(final Map<String, List<String>> queryString) {
        this.queryString = queryString;
    }

    public abstract T create();

    protected abstract String key();

    protected String localizeExpression(final String expr, final Locale locale) {
        return expr.replaceAll("\\{\\{locale\\}\\}", locale.toLanguageTag());
    }

    protected List<String> selectedValues() {
        return queryString.getOrDefault(key(), emptyList());
    }
}
