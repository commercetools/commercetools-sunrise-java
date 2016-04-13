package productcatalog.productoverview.search;

import common.contexts.UserContext;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;
import java.util.Map;

public abstract class FacetSelectorFactory<T> extends SelectorFactory<FacetSelector> {

    private final FacetConfig<T> config;
    protected final UserContext userContext;

    public FacetSelectorFactory(final Map<String, List<String>> queryString, final FacetConfig<T> config, final UserContext userContext) {
        super(queryString);
        this.config = config;
        this.userContext = userContext;
    }

    @Override
    public FacetSelector create() {
        return FacetSelector.of(initializeFacet(), config.getPosition(), selectedValues());
    }

    @Override
    protected String key() {
        return config.getFacetBuilder().getKey();
    }

    protected abstract Facet<ProductProjection> initializeFacet();
}
