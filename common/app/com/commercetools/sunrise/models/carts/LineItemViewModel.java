package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.viewmodels.ViewModel;
import com.commercetools.sunrise.models.products.ProductVariantViewModel;

public class LineItemViewModel extends ViewModel {

    private String lineItemId;
    private long quantity;
    private String totalPrice;
    private ProductVariantViewModel variant;

    public LineItemViewModel() {
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

    public ProductVariantViewModel getVariant() {
        return variant;
    }

    public void setVariant(final ProductVariantViewModel variant) {
        this.variant = variant;
    }
}
