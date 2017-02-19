package com.commercetools.sunrise.common.search.pagination;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedResult;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.commercetools.sunrise.common.forms.QueryStringUtils.findSelectedValueFromQueryString;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class ProductsPerPageSelectorBeanFactory extends ViewModelFactory<ProductsPerPageSelectorBean, PagedResult<ProductProjection>> {

    @Nullable
    private final String selectedOptionValue;
    private final ProductsPerPageFormSettings settings;
    private final ProductsPerPageFormSelectableOptionBeanFactory productsPerPageFormSelectableOptionBeanFactory;

    @Inject
    public ProductsPerPageSelectorBeanFactory(final ProductsPerPageFormSettings settings, final Http.Request httpRequest,
                                              final ProductsPerPageFormSelectableOptionBeanFactory productsPerPageFormSelectableOptionBeanFactory) {
        this.selectedOptionValue = findSelectedValueFromQueryString(settings, httpRequest)
                .map(ProductsPerPageFormOption::getFieldValue)
                .orElse(null);
        this.settings = settings;
        this.productsPerPageFormSelectableOptionBeanFactory = productsPerPageFormSelectableOptionBeanFactory;
    }

    @Override
    protected ProductsPerPageSelectorBean getViewModelInstance() {
        return new ProductsPerPageSelectorBean();
    }

    @Override
    public final ProductsPerPageSelectorBean create(final PagedResult<ProductProjection> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductsPerPageSelectorBean model, final PagedResult<ProductProjection> data) {
        fillKey(model, data);
        fillList(model, data);
    }

    protected void fillKey(final ProductsPerPageSelectorBean model, final PagedResult<ProductProjection> pagedResult) {
        model.setKey(settings.getFieldName());
    }

    protected void fillList(final ProductsPerPageSelectorBean model, final PagedResult<ProductProjection> pagedResult) {
        model.setList(settings.getOptions().stream()
                .map(option -> productsPerPageFormSelectableOptionBeanFactory.create(option, selectedOptionValue))
                .collect(toList()));
    }
}
