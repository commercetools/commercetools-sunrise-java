package com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.commercetools.sunrise.productcatalog.productoverview.search.SearchUtils.selectedValues;

@RequestScoped
public class ProductsPerPageSelectorFactory extends Base {

    private final ProductsPerPageConfig config;
    private final RequestContext requestContext;

    @Inject
    public ProductsPerPageSelectorFactory(final ProductsPerPageConfig config, final RequestContext requestContext) {
        this.config = config;
        this.requestContext = requestContext;
    }

    public ProductsPerPageSelector create() {
        final List<ProductsPerPageOption> options = config.getOptions();
        final ProductsPerPageOption selectedOption = findFirstSelectedOption(selectedValues(config.getKey(), requestContext), options).orElse(null);
        return ProductsPerPageSelector.of(config.getKey(), options, config.getDefaultAmount(), selectedOption);
    }

    private Optional<ProductsPerPageOption> findFirstSelectedOption(final List<String> selectedValues, final List<ProductsPerPageOption> options) {
        return options.stream()
                .filter(option -> selectedValues.contains(option.getValue()))
                .findFirst();
    }
}
