package com.commercetools.sunrise.productcatalog.common;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.LinkBean;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@RequestScoped
public class ProductBreadcrumbBeanFactory extends ViewModelFactory<BreadcrumbBean, ProductBreadcrumbBeanFactory.Data> {

    private final Locale locale;
    private final LocalizedStringResolver localizedStringResolver;
    private final CategoryTree categoryTree;
    private final ProductReverseRouter productReverseRouter;
    private final CategoryBreadcrumbBeanFactory categoryBreadcrumbBeanFactory;

    @Inject
    public ProductBreadcrumbBeanFactory(final Locale locale, final LocalizedStringResolver localizedStringResolver,
                                        final CategoryTree categoryTree, final ProductReverseRouter productReverseRouter,
                                        final CategoryBreadcrumbBeanFactory categoryBreadcrumbBeanFactory) {
        this.locale = locale;
        this.localizedStringResolver = localizedStringResolver;
        this.categoryTree = categoryTree;
        this.productReverseRouter = productReverseRouter;
        this.categoryBreadcrumbBeanFactory = categoryBreadcrumbBeanFactory;
    }

    public final BreadcrumbBean create(final ProductProjection product, final ProductVariant variant) {
        final Data data = new Data(product, variant);
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
        final List<LinkBean> linkBeans = createCategoryLinks(data);
        final List<LinkBean> result = new ArrayList<>(1 + linkBeans.size());
        result.addAll(linkBeans);
        result.add(createProductLink(data));
        breadcrumbBean.setLinks(result);
    }

    private List<LinkBean> createCategoryLinks(final Data data) {
        return data.product.getCategories().stream()
                .findFirst()
                .flatMap(ref -> categoryTree.findById(ref.getId())
                        .map(this::createCategoryTreeLinks))
                .orElseGet(Collections::emptyList);
    }

    private List<LinkBean> createCategoryTreeLinks(final Category category) {
        return categoryBreadcrumbBeanFactory.create(category).getLinks();
    }

    private LinkBean createProductLink(final Data data) {
        final LinkBean linkBean = new LinkBean();
        linkBean.setText(localizedStringResolver.getOrEmpty(data.product.getName()));
        linkBean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, data.product, data.variant));
        return linkBean;
    }

    protected final static class Data extends Base {

        public final ProductProjection product;
        public final ProductVariant variant;

        public Data(final ProductProjection product, final ProductVariant variant) {
            this.product = product;
            this.variant = variant;
        }
    }
}
