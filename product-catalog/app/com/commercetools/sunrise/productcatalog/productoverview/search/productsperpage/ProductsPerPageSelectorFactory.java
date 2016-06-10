package com.commercetools.sunrise.productcatalog.productoverview.search.productsperpage;

import common.contexts.RequestContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

import static com.commercetools.sunrise.productcatalog.productoverview.search.SearchUtils.selectedValues;

public class ProductsPerPageSelectorFactory {

    @Inject
    private ProductsPerPageConfig config;
    @Inject
    private RequestContext requestContext;

    public ProductsPerPageSelector create() {
        final List<ProductsPerPageOption> options = config.getOptions();
        final ProductsPerPageOption selectedOption = findFirstSelectedOption(selectedValues(config.getKey(), requestContext), options).orElse(null);
        return ProductsPerPageSelector.of(config.getKey(), options, config.getDefaultAmount(), selectedOption);
    }

    protected Optional<ProductsPerPageOption> findFirstSelectedOption(final List<String> selectedValues,
                                                                      final List<ProductsPerPageOption> options) {
        return options.stream()
                .filter(option -> selectedValues.contains(option.getValue()))
                .findFirst();
    }
}
