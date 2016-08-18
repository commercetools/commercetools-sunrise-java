package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewPageContent;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class SortSelectorComponent implements ControllerComponent, PageDataReadyHook, ProductProjectionSearchHook {

    @Nullable
    private SortSelector sortSelector;

    @Inject
    private SortSelectorFactory sortSelectorFactory;
    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private I18nIdentifierFactory i18nIdentifierFactory;


    @Override
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        this.sortSelector = sortSelectorFactory.create();
        return search.plusSort(sortSelector.getSelectedSortExpressions());
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (sortSelector != null && pageData.getContent() instanceof ProductOverviewPageContent) {
            final ProductOverviewPageContent content = (ProductOverviewPageContent) pageData.getContent();
            content.setSortSelector(createSortSelector(sortSelector));
        }
    }

    private SortSelectorBean createSortSelector(final SortSelector sortSelector) {
        final SortSelectorBean bean = new SortSelectorBean();
        bean.setKey(sortSelector.getKey());
        bean.setList(sortSelector.getOptions().stream()
                .map(option -> optionToSelectableData(option, sortSelector))
                .collect(toList()));
        return bean;
    }

    private FormSelectableOptionBean optionToSelectableData(final SortOption option, final SortSelector sortSelector) {
        final FormSelectableOptionBean bean = new FormSelectableOptionBean();
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(option.getLabel());
        bean.setLabel(i18nResolver.getOrKey(userContext.locales(), i18nIdentifier));
        bean.setValue(option.getValue());
        bean.setSelected(sortSelector.getSelectedOptions().contains(option));
        return bean;
    }
}
