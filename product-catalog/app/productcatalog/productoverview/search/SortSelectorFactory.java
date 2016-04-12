package productcatalog.productoverview.search;

import common.contexts.UserContext;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SortExpression;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class SortSelectorFactory extends SelectorFactory<SortSelector> {

    private final SortConfig config;
    private final UserContext userContext;

    public SortSelectorFactory(final Map<String, List<String>> queryString, final SortConfig config, final UserContext userContext) {
        super(queryString);
        this.config = config;
        this.userContext = userContext;
    }

    @Override
    public SortSelector create() {
        return SortSelector.of(key(), localizedSortOptions(), selectedValues());
    }

    @Override
    protected String key() {
        return config.getKey();
    }

    public static SortSelectorFactory of(final SortConfig sortConfig, final Map<String, List<String>> queryString,
                                         final UserContext userContext) {
        return new SortSelectorFactory(queryString, sortConfig, userContext);
    }

    private List<SortOption> localizedSortOptions() {
        return config.getOptions().stream()
                .map(option -> {
                    final List<SortExpression<ProductProjection>> localizedExprList = option.getExpressions().stream()
                            .map(expr -> SortExpression.<ProductProjection>of(localizeExpression(expr.expression(), userContext.locale())))
                            .collect(toList());
                    return option.withExpressions(localizedExprList);
                })
                .collect(toList());
    }
}
