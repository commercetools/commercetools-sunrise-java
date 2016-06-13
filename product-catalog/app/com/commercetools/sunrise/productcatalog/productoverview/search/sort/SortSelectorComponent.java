package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.common.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.common.models.FormSelectableOptionBean;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.ControllerComponent;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import com.commercetools.sunrise.productcatalog.hooks.ProductProjectionSearchFilterHook;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewPageContent;

import javax.annotation.Nullable;
import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

public class SortSelectorComponent implements ControllerComponent, SunrisePageDataHook, ProductProjectionSearchFilterHook {

    @Nullable
    private SortSelector sortSelector;

    @Inject
    private SortSelectorFactory sortSelectorFactory;
    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;

    @Override
    public ProductProjectionSearch filterProductProjectionSearch(final ProductProjectionSearch search) {
        this.sortSelector = sortSelectorFactory.create();
        return search.plusSort(sortSelector.getSelectedSortExpressions());
    }

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        if (sortSelector != null && sunrisePageData.getContent() instanceof ProductOverviewPageContent) {
            final ProductOverviewPageContent content = (ProductOverviewPageContent) sunrisePageData.getContent();
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
        final String label = i18nResolver.getOrKey(userContext.locales(), I18nIdentifier.of(option.getLabel()));
        final FormSelectableOptionBean sortOption = new FormSelectableOptionBean(label, option.getValue());
        if (sortSelector.getSelectedOptions().contains(option)) {
            sortOption.setSelected(true);
        }
        return sortOption;
    }
}
