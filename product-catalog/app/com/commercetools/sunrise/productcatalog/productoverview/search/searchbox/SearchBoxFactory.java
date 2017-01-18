package com.commercetools.sunrise.productcatalog.productoverview.search.searchbox;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedStringEntry;
import play.Configuration;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

import static com.commercetools.sunrise.productcatalog.productoverview.search.SearchUtils.selectedValues;
import static java.util.stream.Collectors.joining;

@RequestScoped
public class SearchBoxFactory extends Base {

    private final String key;
    private final Locale locale;
    private final RequestContext requestContext;

    @Inject
    public SearchBoxFactory(final Configuration configuration, final Locale locale, final RequestContext requestContext) {
        this.key = configuration.getString("pop.searchTerm.key", "q");
        this.locale = locale;
        this.requestContext = requestContext;
    }

    public SearchBox create() {
        return SearchBox.of(key, searchTerm().orElse(null));
    }

    private Optional<LocalizedStringEntry> searchTerm() {
        final String term = selectedValues(key, requestContext).stream().collect(joining(" ")).trim();
        if (term.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(LocalizedStringEntry.of(locale, term));
        }
    }
}
