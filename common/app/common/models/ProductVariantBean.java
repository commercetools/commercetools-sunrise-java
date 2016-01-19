package common.models;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.prices.PriceFinder;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.money.MonetaryAmount;
import java.util.Locale;

import static common.utils.PriceUtils.calculateFinalPrice;

public class ProductVariantBean {
    private String name;
    private String sku;
    private String url;
    private String image;
    private String price;
    private String priceOld;

    public ProductVariantBean() {
    }

    public ProductVariantBean(final LineItem lineItem, final UserContext userContext, final ReverseRouter reverseRouter) {
        this.url = reverseRouter.lineItemUrlOrEmpty(userContext.locale(), lineItem);
        fill(lineItem.getName(), lineItem.getVariant(), userContext);
        fillPrice(lineItem.getPrice(), userContext.locale());
    }

    public ProductVariantBean(final ProductProjection product, final ProductVariant variant,
                              final UserContext userContext, final ReverseRouter reverseRouter) {
        this.url = reverseRouter.productUrlOrEmpty(userContext.locale(), product, variant);
        fill(product.getName(), variant, userContext);
        PriceFinder.of(userContext).findPrice(variant.getPrices()).ifPresent(price -> fillPrice(price, userContext.locale()));
    }

    private void fill(final LocalizedString name, final ProductVariant variant, final UserContext userContext) {
        this.name = name.find(userContext.locales()).orElse("");
        this.sku = variant.getSku();
        variant.getImages().stream().findFirst().ifPresent(image -> this.image = image.getUrl());
    }

    private void fillPrice(final Price price, final Locale locale) {
        final MoneyContext moneyContext = MoneyContext.of(price.getValue().getCurrency(), locale);
        final MonetaryAmount currentPrice = calculateFinalPrice(price);
        final boolean hasDiscount = currentPrice.isLessThan(price.getValue());
        if (hasDiscount) {
            this.priceOld = moneyContext.formatOrNull(price);
        }
        this.price = moneyContext.formatOrNull(currentPrice);
    }

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(final String price) {
        this.price = price;
    }

    public String getPriceOld() {
        return priceOld;
    }

    public void setPriceOld(final String priceOld) {
        this.priceOld = priceOld;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(final String sku) {
        this.sku = sku;
    }
}
