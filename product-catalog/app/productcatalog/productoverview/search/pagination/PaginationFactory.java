package productcatalog.productoverview.search.pagination;

import common.contexts.RequestContext;
import common.inject.RequestScoped;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.inject.Inject;

import static productcatalog.productoverview.search.SearchUtils.selectedValues;

@RequestScoped
public class PaginationFactory extends Base {

    public static final int DEFAULT_PAGE = 1;
    private final String key;
    @Inject
    private RequestContext requestContext;

    @Inject
    public PaginationFactory(final Configuration configuration) {
        this.key = configuration.getString("pop.pagination.key", "page");
    }

    public Pagination create() {
        return Pagination.of(key, page());
    }

    private int page() {
        return selectedValues(key, requestContext).stream()
                .findFirst()
                .map(page -> {
                    try {
                        return Integer.valueOf(page);
                    } catch (NumberFormatException e) {
                        return DEFAULT_PAGE;
                    }
                })
                .orElse(DEFAULT_PAGE);
    }
}
