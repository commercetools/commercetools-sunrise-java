package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.CategoryBean;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class PageNavMenuFactory extends ViewModelFactory {

    @Nullable
    private final String saleCategoryExtId;
    private final Locale locale;
    private final LocalizedStringResolver localizedStringResolver;
    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public PageNavMenuFactory(final Configuration configuration, final Locale locale, final LocalizedStringResolver localizedStringResolver,
                              final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter) {
        this.saleCategoryExtId = configuration.getString("common.saleCategoryExternalId");
        this.locale = locale;
        this.localizedStringResolver = localizedStringResolver;
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
    }

    public PageNavMenu create() {
        final PageNavMenu bean = new PageNavMenu();
        initialize(bean);
        return bean;
    }

    protected final void initialize(final PageNavMenu bean) {
        fillCategories(bean);
    }

    protected void fillCategories(final PageNavMenu bean) {
        final List<CategoryBean> categories = new LinkedList<>();
        categoryTree.getRoots().forEach(root -> {
            final CategoryBean categoryData = createCategoryData(root);
            categories.add(categoryData);
        });
        bean.setCategories(categories);
    }

    private CategoryBean createCategoryData(final Category category) {
        final CategoryBean categoryData = new CategoryBean();
        categoryData.setText(localizedStringResolver.getOrEmpty(category.getName()));
        categoryData.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(locale, category));
        categoryData.setSale(Optional.ofNullable(category.getExternalId())
                .map(id -> id.equals(saleCategoryExtId))
                .orElse(false));
        categoryData.setChildren(categoryTree.findChildren(category).stream()
                .map(this::createCategoryData)
                .collect(toList()));
        return categoryData;
    }
}
