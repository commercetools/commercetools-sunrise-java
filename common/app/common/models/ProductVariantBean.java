package common.models;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.prices.PriceFinder;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.money.MonetaryAmount;
import java.util.List;
import java.util.function.Function;

public class ProductVariantBean {
    private String url;
    private String name;
    private String slug;
    private String description;
    private String image;
    private String sku;
    private long quantity;
    private String priceOld;
    private String price;
    private String totalPrice;
    private String variantId;
    private String productId;
    private List<ProductAttributeBean> attributes;

    public ProductVariantBean() {
    }

    public ProductVariantBean(final LineItem lineItem, final ProductDataConfig productDataConfig,
                              final UserContext userContext, final ReverseRouter reverseRouter) {
        this();
        final Function<LocalizedString, String> translator = translate(userContext);
        setName(translator.apply(lineItem.getName()));
        setSlug(translator.apply(lineItem.getProductSlug()));
        //no description available in line item
        fillProductVariantFields(lineItem.getVariant(), productDataConfig, userContext, reverseRouter);
        final MonetaryAmount amountForOneLineItem = calculateAmountForOneLineItem(lineItem);
        final MoneyContext moneyContext = MoneyContext.of(lineItem, userContext);


        final boolean hasDiscount = amountForOneLineItem.isLessThan(lineItem.getPrice().getValue());
        if (hasDiscount) {
            setPriceOld(moneyContext.formatOrNull(lineItem.getPrice()));
        }
        setPrice(moneyContext.formatOrNull(amountForOneLineItem));
        setQuantity(lineItem.getQuantity());
        setTotalPrice(moneyContext.formatOrZero(lineItem.getTotalPrice()));
        setProductId(lineItem.getProductId());
    }

    public ProductVariantBean(final ProductProjection product, final ProductVariant variant, final ProductDataConfig productDataConfig,
                              final UserContext userContext, final ReverseRouter reverseRouter) {
        this();
        final Function<LocalizedString, String> translator = translate(userContext);
        setName(translator.apply(product.getName()));
        setSlug(translator.apply(product.getSlug()));
        setDescription(translator.apply(product.getDescription()));
        setProductId(product.getId());

        fillProductVariantFields(variant, productDataConfig, userContext, reverseRouter);

        PriceFinder.of(userContext).findPrice(variant.getPrices()).ifPresent(price -> {
            final MoneyContext moneyContext = MoneyContext.of(price.getValue().getCurrency(), userContext.locale());
            final boolean hasDiscount = price.getDiscounted() != null;
            if (hasDiscount) {
                setPrice(moneyContext.formatOrNull(price.getDiscounted().getValue()));
                setPriceOld(moneyContext.formatOrNull(price.getValue()));
            } else {
                setPrice(moneyContext.formatOrNull(price.getValue()));
            }
        });
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
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

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(final String sku) {
        this.sku = sku;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(final String slug) {
        this.slug = slug;
    }

    public String getVariantId() {
        return variantId;
    }

    public void setVariantId(final String variantId) {
        this.variantId = variantId;
    }

    public List<ProductAttributeBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(final List<ProductAttributeBean> attributes) {
        this.attributes = attributes;
    }

    public static MonetaryAmount calculateAmountForOneLineItem(final LineItem lineItem) {
        final MonetaryAmount amount;
        final boolean hasProductDiscount = lineItem.getPrice().getDiscounted() != null;
        if (hasProductDiscount) {
            amount = lineItem.getPrice().getDiscounted().getValue();
        } else {
            amount = lineItem.getPrice().getValue();
        }

        return amount;
    }

    private Function<LocalizedString, String> translate(final UserContext userContext) {
        return localizedString -> localizedString != null ? localizedString.find(userContext.locales()).orElse("") : "";
    }

    private void fillProductVariantFields(final ProductVariant variant, final ProductDataConfig productDataConfig,
                                          final UserContext userContext, final ReverseRouter reverseRouter) {
        variant.getImages().stream().findFirst().ifPresent(image -> setImage(image.getUrl()));
        setSku(variant.getSku());
        setVariantId(variant.getId().toString());
        setUrl(reverseRouter.product(userContext.locale().toLanguageTag(), slug, variant.getSku()).url());
        setAttributes(ProductAttributeBean.collect(variant.getAttributes(), productDataConfig, userContext));
    }
}
