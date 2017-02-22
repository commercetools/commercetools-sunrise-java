package com.commercetools.sunrise.common.search.sort;

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
public class SortSelectorViewModelFactory extends ViewModelFactory<SortSelectorViewModel, PagedResult<ProductProjection>> {

    @Nullable
    protected final String selectedOptionValue;
    private final SortFormSettings settings;
    private final SortFormSelectableOptionViewModelFactory sortFormSelectableOptionViewModelFactory;

    @Inject
    public SortSelectorViewModelFactory(final SortFormSettings settings, final Http.Request httpRequest,
                                        final SortFormSelectableOptionViewModelFactory sortFormSelectableOptionViewModelFactory) {
        this.selectedOptionValue = findSelectedValueFromQueryString(settings, httpRequest)
                .map(SortFormOption::getFieldValue)
                .orElse(null);
        this.settings = settings;
        this.sortFormSelectableOptionViewModelFactory = sortFormSelectableOptionViewModelFactory;
    }

    @Override
    protected SortSelectorViewModel getViewModelInstance() {
        return new SortSelectorViewModel();
    }

    @Override
    public final SortSelectorViewModel create(final PagedResult<ProductProjection> data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final SortSelectorViewModel model, final PagedResult<ProductProjection> data) {
        fillKey(model, data);
        fillList(model, data);
    }

    protected void fillKey(final SortSelectorViewModel model, final PagedResult<ProductProjection> pagedResult) {
        model.setKey(settings.getFieldName());
    }

    protected void fillList(final SortSelectorViewModel model, final PagedResult<ProductProjection> pagedResult) {
        model.setList(settings.getOptions().stream()
                .map(option -> sortFormSelectableOptionViewModelFactory.create(option, selectedOptionValue))
                .collect(toList()));
    }
}
