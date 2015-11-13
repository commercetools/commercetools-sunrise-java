package purchase;

import common.contexts.UserContext;
import common.utils.PriceFormatter;
import common.utils.ZeroPriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.ProductVariant;

import javax.money.MonetaryAmount;
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

    public ProductVariantBean(final LineItem lineItem, final UserContext userContext) {
        this();
        final Function<LocalizedString, String> tr = ls -> ls != null ? ls.find(userContext.locales()).orElse("") : "";
        setName(tr.apply(lineItem.getName()));
        setSlug(tr.apply(lineItem.getProductSlug()));
        //no description available in line item
        fillProductVariantFields(lineItem.getVariant());
        final MonetaryAmount amountForOneLineItem = CartOrderBean.calculateAmountForOneLineItem(lineItem);
        final ZeroPriceFormatter priceFormatter = PriceFormatter.of(userContext.locale(), userContext.currency());
        final boolean hasDiscount = amountForOneLineItem.isLessThan(lineItem.getPrice().getValue());
        if (hasDiscount) {
            setPriceOld(priceFormatter.format(lineItem.getPrice().getValue()));
        }
        setPrice(priceFormatter.format(amountForOneLineItem));
        setQuantity(lineItem.getQuantity());
        setTotal(priceFormatter.format(lineItem.getTotalPrice()));
        setProductId(lineItem.getProductId());
    }

    private void fillProductVariantFields(final ProductVariant variant) {
        setImage(variant.getImages().stream().findFirst().map(i -> i.getUrl()).orElse(""));
        setSku(variant.getSku());
        setVariantId("" + variant.getId());
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
}
