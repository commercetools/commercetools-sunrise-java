package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class CategoryBeanFactory extends ViewModelFactory<CategoryBean, Category> {

    @Nullable
    private final String saleCategoryExtId;
    private final Locale locale;
    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public CategoryBeanFactory(final Configuration configuration, final Locale locale, final CategoryTree categoryTree,
                               final ProductReverseRouter productReverseRouter) {
        this.saleCategoryExtId = configuration.getString("common.saleCategoryExternalId");
        this.locale = locale;
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
    }

    @Override
    protected CategoryBean getViewModelInstance() {
        return new CategoryBean();
    }

    @Override
    public final CategoryBean create(final Category data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CategoryBean model, final Category data) {
        fillText(model, data);
        fillUrl(model, data);
        fillSale(model, data);
        fillChildren(model, data);
    }

    protected void fillText(final CategoryBean model, final Category category) {
        model.setText(category.getName());
    }

    protected void fillUrl(final CategoryBean model, final Category category) {
        model.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(locale, category));
    }

    protected void fillSale(final CategoryBean model, final Category category) {
        model.setSale(Optional.ofNullable(category.getExternalId())
                .map(id -> id.equals(saleCategoryExtId))
                .orElse(false));
    }

    protected void fillChildren(final CategoryBean model, final Category category) {
        model.setChildren(categoryTree.findChildren(category).stream()
                .map(this::create)
                .collect(toList()));
    }
}
