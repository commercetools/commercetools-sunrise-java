package com.commercetools.sunrise.framework.checkout;

public enum CheckoutStep {

    ADDRESS, SHIPPING, PAYMENT, CONFIRMATION;

    public boolean isAddressStepActive() {
        return isStep(ADDRESS);
    }

    public boolean isShippingStepActive() {
        return isStep(SHIPPING);
    }

    public boolean isPaymentStepActive() {
        return isStep(PAYMENT);
    }

    public boolean isConfirmationStepActive() {
        return isStep(CONFIRMATION);
    }

    private boolean isStep(final CheckoutStep what) {
        return name().equals(what.name());
    }
}
