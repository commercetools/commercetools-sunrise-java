package com.commercetools.sunrise.productcatalog.productdetail.view;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.models.ProductWithVariant;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.productcatalog.common.ProductBeanFactory;

import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

public class ProductDetailPageContentFactory extends PageContentFactory<ProductDetailPageContent, ProductWithVariant> {

    private final Locale locale;
    private final PageTitleResolver pageTitleResolver;
    private final ProductBreadcrumbBeanFactory productBreadcrumbBeanFactory;
    private final ProductBeanFactory productBeanFactory;

    @Inject
    public ProductDetailPageContentFactory(final Locale locale, final PageTitleResolver pageTitleResolver,
                                           final ProductBreadcrumbBeanFactory productBreadcrumbBeanFactory, final ProductBeanFactory productBeanFactory) {
        this.locale = locale;
        this.pageTitleResolver = pageTitleResolver;
        this.productBreadcrumbBeanFactory = productBreadcrumbBeanFactory;
        this.productBeanFactory = productBeanFactory;
    }

    @Override
    protected ProductDetailPageContent getViewModelInstance() {
        return new ProductDetailPageContent();
    }

    @Override
    public final ProductDetailPageContent create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected final void initialize(final ProductDetailPageContent model, final ProductWithVariant productWithVariant) {
        super.initialize(model, productWithVariant);
        fillProduct(model, productWithVariant);
        fillBreadCrumb(model, productWithVariant);
    }

    @Override
    protected void fillTitle(final ProductDetailPageContent model, final ProductWithVariant productWithVariant) {
        final String title = String.format("%s %s",
                productWithVariant.getProduct().getName().find(singletonList(locale)).orElse(""),
                pageTitleResolver.getOrEmpty("catalog:productDetailPage.title"));
        model.setTitle(title);
    }

    protected void fillBreadCrumb(final ProductDetailPageContent content, final ProductWithVariant productWithVariant) {
        content.setBreadcrumb(productBreadcrumbBeanFactory.create(productWithVariant));
    }

    protected void fillProduct(final ProductDetailPageContent content, final ProductWithVariant productWithVariant) {
        content.setProduct(productBeanFactory.create(productWithVariant));
    }
}
