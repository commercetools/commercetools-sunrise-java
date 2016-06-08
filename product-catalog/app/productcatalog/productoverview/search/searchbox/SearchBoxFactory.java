package productcatalog.productoverview.search.searchbox;

import common.contexts.RequestContext;
import common.contexts.UserContext;
import common.inject.RequestScoped;
import io.sphere.sdk.models.LocalizedStringEntry;
import play.Configuration;

import javax.inject.Inject;
import java.util.Optional;

import static java.util.stream.Collectors.joining;
import static productcatalog.productoverview.search.SearchUtils.selectedValues;

@RequestScoped
public class SearchBoxFactory {

    private final String key;
    @Inject
    private UserContext userContext;
    @Inject
    private RequestContext requestContext;

    @Inject
    public SearchBoxFactory(final Configuration configuration) {
        this.key = configuration.getString("pop.searchTerm.key", "q");
    }

    public SearchBox create() {
        return SearchBox.of(key, searchTerm().orElse(null));
    }

    protected Optional<LocalizedStringEntry> searchTerm() {
        final String term = selectedValues(key, requestContext).stream().collect(joining(" ")).trim();
        if (term.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(LocalizedStringEntry.of(userContext.locale(), term));
        }
    }
}
