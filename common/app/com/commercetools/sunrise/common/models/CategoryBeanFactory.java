package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class CategoryBeanFactory extends ViewModelFactory<CategoryBean, CategoryBeanFactory.Data> {

    @Nullable
    private final String saleCategoryExtId;
    private final Locale locale;
    private final CategoryTree categoryTree;
    private final LocalizedStringResolver localizedStringResolver;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public CategoryBeanFactory(final Configuration configuration, final Locale locale, final CategoryTree categoryTree,
                               final LocalizedStringResolver localizedStringResolver, final ProductReverseRouter productReverseRouter) {
        this.saleCategoryExtId = configuration.getString("common.saleCategoryExternalId");
        this.locale = locale;
        this.categoryTree = categoryTree;
        this.localizedStringResolver = localizedStringResolver;
        this.productReverseRouter = productReverseRouter;
    }

    public final CategoryBean create(final Category category) {
        final Data data = new Data(category);
        return initializedViewModel(data);
    }

    @Override
    protected CategoryBean getViewModelInstance() {
        return new CategoryBean();
    }

    @Override
    protected final void initialize(final CategoryBean bean, final Data data) {
        fillText(bean, data);
        fillUrl(bean, data);
        fillSale(bean, data);
        fillChildren(bean, data);
    }

    protected void fillText(final CategoryBean bean, final Data data) {
        bean.setText(localizedStringResolver.getOrEmpty(data.category.getName()));
    }

    protected void fillUrl(final CategoryBean bean, final Data data) {
        bean.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(locale, data.category));
    }

    protected void fillSale(final CategoryBean bean, final Data data) {
        bean.setSale(Optional.ofNullable(data.category.getExternalId())
                .map(id -> id.equals(saleCategoryExtId))
                .orElse(false));
    }

    protected void fillChildren(final CategoryBean bean, final Data data) {
        bean.setChildren(categoryTree.findChildren(data.category).stream()
                .map(this::create)
                .collect(toList()));
    }

    protected final static class Data extends Base {

        public final Category category;

        public Data(final Category category) {
            this.category = category;
        }
    }
}
