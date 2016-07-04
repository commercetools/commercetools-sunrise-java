package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.ProductProjectionSearchFilterHook;
import com.commercetools.sunrise.hooks.PageDataHook;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewPageContent;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class SortSelectorComponent implements ControllerComponent, PageDataHook, ProductProjectionSearchFilterHook {

    @Nullable
    private SortSelector sortSelector;

    @Inject
    private SortSelectorFactory sortSelectorFactory;
    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;

    @Override
    public ProductProjectionSearch filterQuery(final ProductProjectionSearch search) {
        this.sortSelector = sortSelectorFactory.create();
        return search.plusSort(sortSelector.getSelectedSortExpressions());
    }

    @Override
    public void acceptPageData(final PageData pageData) {
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
        bean.setLabel(i18nResolver.getOrKey(userContext.locales(), I18nIdentifier.of(option.getLabel())));
        bean.setValue(option.getValue());
        bean.setSelected(sortSelector.getSelectedOptions().contains(option));
        return bean;
    }
}
