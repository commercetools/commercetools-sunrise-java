package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedStringEntry;

import javax.annotation.Nullable;
import java.util.Optional;

public class SearchBox extends Base {

    private final String key;
    private final Optional<LocalizedStringEntry> searchTerm;

    public SearchBox(final String key, @Nullable final LocalizedStringEntry searchTerm) {
        this.key = key;
        this.searchTerm = Optional.ofNullable(searchTerm);
    }

    public String getKey() {
        return key;
    }

    public Optional<LocalizedStringEntry> getSearchTerm() {
        return searchTerm;
    }

    public static SearchBox of(final String key, @Nullable LocalizedStringEntry searchTerm) {
        return new SearchBox(key, searchTerm);
    }
}