package common.models;

import common.contexts.UserContext;
import common.utils.MoneyContext;
import common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.ProductVariant;

import javax.money.MonetaryAmount;
import java.util.List;
import java.util.function.Function;

public class ProductVariantBean {
    private String name;
    private String slug;
    private String description;
    private String image;
    private String sku;
    private long quantity;
    private String priceOld;
    private String price;
    private String total;
    private String variantId;
    private String productId;
    private List<ProductAttributeBean> attributes;

    public ProductVariantBean(final LineItem lineItem, final UserContext userContext, final ProductDataConfig productDataConfig) {
        this();
        final Function<LocalizedString, String> tr = ls -> ls != null ? ls.find(userContext.locales()).orElse("") : "";
        setName(tr.apply(lineItem.getName()));
        setSlug(tr.apply(lineItem.getProductSlug()));
        //no description available in line item
        fillProductVariantFields(lineItem.getVariant(), productDataConfig, userContext);
        final MonetaryAmount amountForOneLineItem = calculateAmountForOneLineItem(lineItem);
        final MoneyContext moneyContext = MoneyContext.of(lineItem, userContext);


        final boolean hasDiscount = amountForOneLineItem.isLessThan(lineItem.getPrice().getValue());
        if (hasDiscount) {
            setPriceOld(moneyContext.formatOrNull(lineItem.getPrice()));
        }
        setPrice(moneyContext.formatOrNull(amountForOneLineItem));
        setQuantity(lineItem.getQuantity());
        setTotal(moneyContext.formatOrZero(lineItem.getTotalPrice()));
        setProductId(lineItem.getProductId());
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

    private void fillProductVariantFields(final ProductVariant variant, final ProductDataConfig productDataConfig, final UserContext userContext) {
        setImage(variant.getImages().stream().findFirst().map(i -> i.getUrl()).orElse(""));
        setSku(variant.getSku());
        setVariantId("" + variant.getId());
        setAttributes(ProductAttributeBean.collect(variant.getAttributes(), productDataConfig, userContext));
    }

    public ProductVariantBean() {

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

    public String getTotal() {
        return total;
    }

    public void setTotal(final String total) {
        this.total = total;
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
}
