package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.productcatalog.common.BreadcrumbBeanFactory;
import com.commercetools.sunrise.productcatalog.common.ProductBeanFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
public class ProductDetailPageContentFactory extends ViewModelFactory {

    private final Locale locale;
    private final LocalizedStringResolver localizedStringResolver;
    private final CartReverseRouter cartReverseRouter;
    private final BreadcrumbBeanFactory breadcrumbBeanFactory;
    private final ProductBeanFactory productBeanFactory;

    @Inject
    public ProductDetailPageContentFactory(final Locale locale, final LocalizedStringResolver localizedStringResolver,
                                           final CartReverseRouter cartReverseRouter, final BreadcrumbBeanFactory breadcrumbBeanFactory,
                                           final ProductBeanFactory productBeanFactory) {
        this.locale = locale;
        this.localizedStringResolver = localizedStringResolver;
        this.cartReverseRouter = cartReverseRouter;
        this.breadcrumbBeanFactory = breadcrumbBeanFactory;
        this.productBeanFactory = productBeanFactory;
    }

    public ProductDetailPageContent create(final ProductProjection product, final ProductVariant variant) {
        final ProductDetailPageContent bean = new ProductDetailPageContent();
        initialize(bean, product, variant);
        return bean;
    }

    protected final void initialize(final ProductDetailPageContent bean, final ProductProjection product, final ProductVariant variant) {
        fillAdditionalTitle(product, bean);
        fillProduct(product, variant, bean);
        fillBreadCrumb(product, variant, bean);
        fillAddToCartFormUrl(bean);
    }

    protected void fillAddToCartFormUrl(final ProductDetailPageContent content) {
        content.setAddToCartFormUrl(cartReverseRouter.processAddProductToCartForm(locale.toLanguageTag()).url()); // TODO move to page meta
    }

    protected void fillAdditionalTitle(final ProductProjection product, final ProductDetailPageContent content) {
        content.setTitle(localizedStringResolver.getOrEmpty(product.getName()));
    }

    protected void fillBreadCrumb(final ProductProjection product, final ProductVariant variant, final ProductDetailPageContent content) {
        content.setBreadcrumb(breadcrumbBeanFactory.create(product, variant));
    }

    protected void fillProduct(final ProductProjection product, final ProductVariant variant, final ProductDetailPageContent content) {
        content.setProduct(productBeanFactory.create(product, variant));
    }
}
