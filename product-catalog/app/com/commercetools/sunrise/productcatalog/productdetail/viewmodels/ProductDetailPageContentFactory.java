package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContentFactory;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;

import javax.inject.Inject;
import java.util.Locale;

import static java.util.Collections.singletonList;

public class ProductDetailPageContentFactory extends PageContentFactory<ProductDetailPageContent, ProductWithVariant> {

    private final Locale locale;
    private final PageTitleResolver pageTitleResolver;
    private final ProductBreadcrumbViewModelFactory productBreadcrumbViewModelFactory;
    private final ProductViewModelFactory productViewModelFactory;

    @Inject
    public ProductDetailPageContentFactory(final Locale locale, final PageTitleResolver pageTitleResolver,
                                           final ProductBreadcrumbViewModelFactory productBreadcrumbViewModelFactory, final ProductViewModelFactory productViewModelFactory) {
        this.locale = locale;
        this.pageTitleResolver = pageTitleResolver;
        this.productBreadcrumbViewModelFactory = productBreadcrumbViewModelFactory;
        this.productViewModelFactory = productViewModelFactory;
    }

    protected final Locale getLocale() {
        return locale;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    protected final ProductBreadcrumbViewModelFactory getProductBreadcrumbViewModelFactory() {
        return productBreadcrumbViewModelFactory;
    }

    protected final ProductViewModelFactory getProductViewModelFactory() {
        return productViewModelFactory;
    }

    @Override
    protected ProductDetailPageContent newViewModelInstance(final ProductWithVariant productWithVariant) {
        return new ProductDetailPageContent();
    }

    @Override
    public final ProductDetailPageContent create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected final void initialize(final ProductDetailPageContent viewModel, final ProductWithVariant productWithVariant) {
        super.initialize(viewModel, productWithVariant);
        fillProduct(viewModel, productWithVariant);
        fillBreadCrumb(viewModel, productWithVariant);
    }

    @Override
    protected void fillTitle(final ProductDetailPageContent viewModel, final ProductWithVariant productWithVariant) {
        final String title = String.format("%s %s",
                productWithVariant.getProduct().getName().find(singletonList(locale)).orElse(""),
                pageTitleResolver.getOrEmpty("catalog:productDetailPage.title"));
        viewModel.setTitle(title);
    }

    protected void fillBreadCrumb(final ProductDetailPageContent content, final ProductWithVariant productWithVariant) {
        content.setBreadcrumb(productBreadcrumbViewModelFactory.create(productWithVariant));
    }

    protected void fillProduct(final ProductDetailPageContent content, final ProductWithVariant productWithVariant) {
        content.setProduct(productViewModelFactory.create(productWithVariant));
    }
}
