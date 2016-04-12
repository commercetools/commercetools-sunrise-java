package productcatalog.productoverview.search;

import common.contexts.UserContext;
import io.sphere.sdk.models.LocalizedStringEntry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

public class SearchBoxFactory extends SelectorFactory<SearchBox> {

    private final SearchConfig config;
    private final UserContext userContext;

    public SearchBoxFactory(final Map<String, List<String>> queryString, final SearchConfig config, final UserContext userContext) {
        super(queryString);
        this.config = config;
        this.userContext = userContext;
    }

    @Override
    public SearchBox create() {
        return SearchBox.of(key(), searchTerm().orElse(null));
    }

    @Override
    protected String key() {
        return config.getSearchTermKey();
    }

    public static SearchBoxFactory of(final SearchConfig searchConfig,
                                      final Map<String, List<String>> queryString, final UserContext userContext) {
        return new SearchBoxFactory(queryString, searchConfig, userContext);
    }

    public Optional<LocalizedStringEntry> searchTerm() {
        final String term = selectedValues().stream().collect(joining(" ")).trim();
        if (term.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(LocalizedStringEntry.of(userContext.locale(), term));
        }
    }
}
