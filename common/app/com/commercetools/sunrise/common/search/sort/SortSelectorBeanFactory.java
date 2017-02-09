package com.commercetools.sunrise.common.search.sort;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.queries.PagedResult;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.commercetools.sunrise.common.forms.FormUtils.findSelectedValueFromRequest;
import static java.util.stream.Collectors.toList;

@RequestScoped
public class SortSelectorBeanFactory extends ViewModelFactory<SortSelectorBean, PagedResult<ProductProjection>> {

    @Nullable
    protected final String selectedOptionValue;
    private final SortFormSettings settings;
    private final SortFormSelectableOptionBeanFactory sortFormSelectableOptionBeanFactory;

    @Inject
    public SortSelectorBeanFactory(final SortFormSettings settings, final RequestContext requestContext,
                                   final SortFormSelectableOptionBeanFactory sortFormSelectableOptionBeanFactory) {
        this.selectedOptionValue = findSelectedValueFromRequest(settings, requestContext)
                .map(SortFormOption::getFieldValue)
                .orElse(null);
        this.settings = settings;
        this.sortFormSelectableOptionBeanFactory = sortFormSelectableOptionBeanFactory;
    }

    @Override
    protected SortSelectorBean getViewModelInstance() {
        return new SortSelectorBean();
    }

    @Override
    public final SortSelectorBean create(final PagedResult<ProductProjection> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final SortSelectorBean model, final PagedResult<ProductProjection> data) {
        fillKey(model, data);
        fillList(model, data);
    }

    protected void fillKey(final SortSelectorBean model, final PagedResult<ProductProjection> pagedResult) {
        model.setKey(settings.getFieldName());
    }

    protected void fillList(final SortSelectorBean model, final PagedResult<ProductProjection> pagedResult) {
        model.setList(settings.getOptions().stream()
                .map(option -> sortFormSelectableOptionBeanFactory.create(option, selectedOptionValue))
                .collect(toList()));
    }
}
