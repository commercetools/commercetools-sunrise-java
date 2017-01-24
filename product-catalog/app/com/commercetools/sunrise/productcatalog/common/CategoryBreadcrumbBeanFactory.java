package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.LinkBean;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class CategoryBreadcrumbBeanFactory extends ViewModelFactory<BreadcrumbBean, CategoryBreadcrumbBeanFactory.Data> {

    private final Locale locale;
    private final CategoryTree categoryTree;
    private final LocalizedStringResolver localizedStringResolver;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public CategoryBreadcrumbBeanFactory(final Locale locale, final CategoryTree categoryTree,
                                         final LocalizedStringResolver localizedStringResolver, final ProductReverseRouter productReverseRouter) {
        this.locale = locale;
        this.categoryTree = categoryTree;
        this.localizedStringResolver = localizedStringResolver;
        this.productReverseRouter = productReverseRouter;
    }

    public final BreadcrumbBean create(final Category category) {
        final Data data = new Data(category);
        return initializedViewModel(data);
    }

    @Override
    protected BreadcrumbBean getViewModelInstance() {
        return new BreadcrumbBean();
    }

    @Override
    protected final void initialize(final BreadcrumbBean bean, final Data data) {
        fillLinks(bean, data);
    }

    protected void fillLinks(final BreadcrumbBean breadcrumbBean, final Data data) {
        final List<LinkBean> categoryTreeLinks = getCategoryWithAncestors(data.category).stream()
                .map(this::createCategoryLinkData)
                .collect(toList());
        breadcrumbBean.setLinks(categoryTreeLinks);
    }

    private List<Category> getCategoryWithAncestors(final Category category) {
        final List<Category> ancestors = category.getAncestors().stream()
                .map(ref -> categoryTree.findById(ref.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
        ancestors.add(category);
        return ancestors;
    }

    private LinkBean createCategoryLinkData(final Category category) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(localizedStringResolver.getOrEmpty(category.getName()));
        linkBean.setUrl(productReverseRouter.productOverviewPageUrlOrEmpty(locale, category));
        return linkBean;
    }

    protected final static class Data extends Base {

        public final Category category;

        public Data(final Category category) {
            this.category = category;
        }
    }
}
