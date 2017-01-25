package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.productcatalog.common.ProductBeanFactory;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class ProductDetailPageContentFactory extends PageContentFactory<ProductDetailPageContent, ProductDetailPageData> {

    private final Locale locale;
    private final LocalizedStringResolver localizedStringResolver;
    private final PageTitleResolver pageTitleResolver;
    private final CartReverseRouter cartReverseRouter;
    private final ProductBreadcrumbBeanFactory productBreadcrumbBeanFactory;
    private final ProductBeanFactory productBeanFactory;

    @Inject
    public ProductDetailPageContentFactory(final Locale locale, final LocalizedStringResolver localizedStringResolver,
                                           final PageTitleResolver pageTitleResolver, final CartReverseRouter cartReverseRouter,
                                           final ProductBreadcrumbBeanFactory productBreadcrumbBeanFactory, final ProductBeanFactory productBeanFactory) {
        this.locale = locale;
        this.localizedStringResolver = localizedStringResolver;
        this.pageTitleResolver = pageTitleResolver;
        this.cartReverseRouter = cartReverseRouter;
        this.productBreadcrumbBeanFactory = productBreadcrumbBeanFactory;
        this.productBeanFactory = productBeanFactory;
    }

    @Override
    protected ProductDetailPageContent getViewModelInstance() {
        return new ProductDetailPageContent();
    }

    @Override
    public final ProductDetailPageContent create(final ProductDetailPageData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final ProductDetailPageContent model, final ProductDetailPageData data) {
        super.initialize(model, data);
        fillProduct(model, data);
        fillBreadCrumb(model, data);
        fillAddToCartFormUrl(model, data);
    }

    @Override
    protected void fillTitle(final ProductDetailPageContent model, final ProductDetailPageData data) {
        final String title = String.format("%s %s",
                localizedStringResolver.getOrEmpty(data.productWithVariant.getProduct().getName()),
                pageTitleResolver.getOrEmpty("catalog:productDetailPage.title"));
        model.setTitle(title);
    }

    protected void fillAddToCartFormUrl(final ProductDetailPageContent content, final ProductDetailPageData data) {
        content.setAddToCartFormUrl(cartReverseRouter.processAddProductToCartForm(locale.toLanguageTag()).url()); // TODO move to page meta
    }

    protected void fillBreadCrumb(final ProductDetailPageContent content, final ProductDetailPageData data) {
        content.setBreadcrumb(productBreadcrumbBeanFactory.create(data));
    }

    protected void fillProduct(final ProductDetailPageContent content, final ProductDetailPageData data) {
        content.setProduct(productBeanFactory.create(data.productWithVariant));
    }
}
