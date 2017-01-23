package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

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
import java.util.Locale;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public final class SortSelectorComponent implements ControllerComponent, PageDataReadyHook, ProductProjectionSearchHook {

    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;
    private final SortSelectorFactory sortSelectorFactory;

    @Nullable
    private SortSelector sortSelector;

    @Inject
    public SortSelectorComponent(final Locale locale, final I18nResolver i18nResolver,final I18nIdentifierFactory i18nIdentifierFactory,
                                 final SortSelectorFactory sortSelectorFactory) {
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
        this.sortSelectorFactory = sortSelectorFactory;
    }

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
        bean.setLabel(i18nResolver.getOrKey(singletonList(locale), i18nIdentifier));
        bean.setValue(option.getValue());
        bean.setSelected(sortSelector.getSelectedOptions().contains(option));
        return bean;
    }
}
