package shoppingcart;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class AddToCartFormData extends Base {
    public interface Validation {}

    private String csrfToken;

    @Constraints.Required(groups = Validation.class)
    private String productId;
    @Constraints.Required(groups = Validation.class)
    private int variantId;
    @Constraints.Required(groups = Validation.class)
    private long quantity;

    public AddToCartFormData() {
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }

    public int getVariantId() {
        return variantId;
    }

    public void setVariantId(final int variantId) {
        this.variantId = variantId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
