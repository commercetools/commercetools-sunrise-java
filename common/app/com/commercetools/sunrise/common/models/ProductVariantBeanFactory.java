package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.prices.PriceFinder;
import com.commercetools.sunrise.common.utils.MoneyContext;
import com.commercetools.sunrise.common.utils.PriceUtils;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.*;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;

import javax.inject.Inject;
import javax.money.MonetaryAmount;
import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.firstNonNull;

public class ProductVariantBeanFactory extends Base {

    @Inject
    protected UserContext userContext;
    @Inject
    protected ProductReverseRouter productReverseRouter;

    public ProductVariantBean create(final ProductProjection product, final ProductVariant variant) {
        final ProductVariantBean bean = new ProductVariantBean();
        fillVariantInfo(bean, variant);
        fillPrices(variant, bean);
        fillName(product, bean);
        fillUrl(product, variant, bean);
        return bean;
    }

    public ProductVariantBean create(final LineItem lineItem) {
        final ProductVariantBean bean = new ProductVariantBean();
        fillVariantInfo(bean, lineItem.getVariant());
        fillPriceInfo(bean, lineItem.getPrice());
        fillName(lineItem, bean);
        fillUrl(lineItem, bean);
        return bean;
    }

    protected void fillUrl(final ProductProjection product, final ProductVariant variant, final ProductVariantBean bean) {
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(userContext.locale(), product, variant));
    }

    protected void fillUrl(final LineItem lineItem, final ProductVariantBean bean) {
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(userContext.locale(), lineItem));
    }

    protected void fillName(final ProductProjection product, final ProductVariantBean bean) {
        bean.setName(createName(product.getName()));
    }

    protected void fillPrices(final ProductVariant variant, final ProductVariantBean bean) {
        resolvePrice(variant).ifPresent(price -> fillPriceInfo(bean, price));
    }

    protected Optional<PriceLike> resolvePrice(final ProductVariant variant) {
        PriceLike price = firstNonNull(variant.getPrice(), variant.getScopedPrice());
        return Optional.ofNullable(price);
    }

    protected void fillName(final LineItem lineItem, final ProductVariantBean bean) {
        bean.setName(createName(lineItem.getName()));
    }

    protected void fillVariantInfo(final ProductVariantBean bean, final ProductVariant variant) {
        bean.setSku(variant.getSku());
        createImage(variant).ifPresent(bean::setImage);
    }

    protected void fillPriceInfo(final ProductVariantBean bean, final PriceLike price) {
        final MoneyContext moneyContext = MoneyContext.of(price.getValue().getCurrency(), userContext.locale());
        final MonetaryAmount currentPrice = PriceUtils.calculateFinalPrice(price);
        final boolean hasDiscount = currentPrice.isLessThan(price.getValue());
        if (hasDiscount) {
            bean.setPriceOld(moneyContext.formatOrNull(price));
        }
        bean.setPrice(moneyContext.formatOrNull(currentPrice));
    }

    protected Optional<String> createImage(final ProductVariant variant) {
        return variant.getImages().stream().findFirst().map(Image::getUrl);
    }

    protected String createName(final LocalizedString name) {
        return name.find(userContext.locales()).orElse("");
    }
}
