package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import com.commercetools.sunrise.common.utils.PriceUtils;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RequestScoped
public class ProductVariantBeanFactory extends ViewModelFactory {

    private final Locale locale;
    private final List<Locale> locales;
    private final PriceFormatter priceFormatter;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    public ProductVariantBeanFactory(final UserContext userContext, final PriceFormatter priceFormatter,
                                     final ProductReverseRouter productReverseRouter) {
        this.locale = userContext.locale();
        this.locales = userContext.locales();
        this.priceFormatter = priceFormatter;
        this.productReverseRouter = productReverseRouter;
    }

    public ProductVariantBean create(final ProductProjection product, final ProductVariant variant) {
        final ProductVariantBean bean = new ProductVariantBean();
        initialize(bean, product, variant);
        return bean;
    }

    public ProductVariantBean create(final LineItem lineItem) {
        final ProductVariantBean bean = new ProductVariantBean();
        initialize(bean, lineItem);
        return bean;
    }

    protected final void initialize(final ProductVariantBean bean, final LineItem lineItem) {
        fillSku(bean, lineItem.getVariant());
        fillImage(bean, lineItem.getVariant());
        fillPrices(bean, lineItem);
        fillName(bean, lineItem);
        fillUrl(bean, lineItem);
    }

    protected final void initialize(final ProductVariantBean bean, final ProductProjection product, final ProductVariant variant) {
        fillSku(bean, variant);
        fillImage(bean, variant);
        fillPrices(bean, product, variant);
        fillName(bean, product, variant);
        fillUrl(bean, product, variant);
    }

    protected void fillUrl(final ProductVariantBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, product, variant));
    }

    protected void fillUrl(final ProductVariantBean bean, final LineItem lineItem) {
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(locale, lineItem));
    }

    protected void fillName(final ProductVariantBean bean, final ProductProjection product, final ProductVariant variant) {
        bean.setName(createName(product.getName()));
    }

    protected void fillName(final ProductVariantBean bean, final LineItem lineItem) {
        bean.setName(createName(lineItem.getName()));
    }

    protected void fillPrices(final ProductVariantBean bean, final ProductProjection product, final ProductVariant variant) {
        PriceUtils.findAppliedProductPrice(variant).ifPresent(price -> bean.setPrice(priceFormatter.format(price)));
        PriceUtils.findPreviousProductPrice(variant).ifPresent(oldPrice -> bean.setPriceOld(priceFormatter.format(oldPrice)));
    }

    protected void fillImage(final ProductVariantBean bean, final ProductVariant variant) {
        createImage(variant).ifPresent(bean::setImage);
    }

    protected void fillSku(final ProductVariantBean bean, final ProductVariant variant) {
        bean.setSku(variant.getSku());
    }

    protected void fillPrices(final ProductVariantBean bean, final LineItem lineItem) {
        bean.setPrice(priceFormatter.format(PriceUtils.findAppliedProductPrice(lineItem)));
        PriceUtils.findPreviousProductPrice(lineItem).ifPresent(oldPrice -> bean.setPriceOld(priceFormatter.format(oldPrice)));
    }

    private Optional<String> createImage(final ProductVariant variant) {
        return variant.getImages().stream().findFirst().map(Image::getUrl);
    }

    private String createName(final LocalizedString name) {
        return name.find(locales).orElse("");
    }
}