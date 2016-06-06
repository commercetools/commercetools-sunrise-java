package common.models;

import common.contexts.UserContext;
import common.prices.PriceFinder;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import wedecidelatercommon.ProductReverseRouter;

import javax.money.MonetaryAmount;
import java.util.Optional;

import static common.utils.PriceUtils.calculateFinalPrice;

public class ProductVariantBeanFactoryInjectless {

    public static ProductVariantBean create(final ProductProjection product, final ProductVariant variant,
                                            final UserContext userContext, final ProductReverseRouter productReverseRouter) {
        final ProductVariantBean bean = new ProductVariantBean();
        fillVariantInfo(bean, variant);
        PriceFinder.of(userContext).findPrice(variant.getPrices()).ifPresent(price -> fillPriceInfo(bean, price, userContext));
        bean.setName(createName(product.getName(), userContext));
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(userContext.locale(), product, variant));
        return bean;
    }

    public static ProductVariantBean create(final LineItem lineItem, final ProductReverseRouter productReverseRouter, final UserContext userContext) {
        final ProductVariantBean bean = new ProductVariantBean();
        fillVariantInfo(bean, lineItem.getVariant());
        fillPriceInfo(bean, lineItem.getPrice(), userContext);
        bean.setName(createName(lineItem.getName(), userContext));
        bean.setUrl(productReverseRouter.productDetailPageUrlOrEmpty(userContext.locale(), lineItem));
        return bean;
    }

    private static void fillVariantInfo(final ProductVariantBean bean, final ProductVariant variant) {
        bean.setSku(variant.getSku());
        createImage(variant).ifPresent(bean::setImage);
    }

    private static void fillPriceInfo(final ProductVariantBean bean, final Price price, final UserContext userContext) {
        final MoneyContext moneyContext = MoneyContext.of(price.getValue().getCurrency(), userContext.locale());
        final MonetaryAmount currentPrice = calculateFinalPrice(price);
        final boolean hasDiscount = currentPrice.isLessThan(price.getValue());
        if (hasDiscount) {
            bean.setPriceOld(moneyContext.formatOrNull(price));
        }
        bean.setPrice(moneyContext.formatOrNull(currentPrice));
    }

    private static Optional<String> createImage(final ProductVariant variant) {
        return variant.getImages().stream().findFirst().map(Image::getUrl);
    }

    private static String createName(final LocalizedString productName, final UserContext userContext) {
        return productName.find(userContext.locales()).orElse("");
    }
}
