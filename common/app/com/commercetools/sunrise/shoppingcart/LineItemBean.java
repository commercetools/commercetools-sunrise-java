package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.ViewModel;
import com.commercetools.sunrise.common.models.ProductVariantBean;

public class LineItemBean extends ViewModel {

    private String lineItemId;
    private long quantity;
    private String totalPrice;
    private ProductVariantBean variant;

    public LineItemBean() {
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(final String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ProductVariantBean getVariant() {
        return variant;
    }

    public void setVariant(final ProductVariantBean variant) {
        this.variant = variant;
    }
}
