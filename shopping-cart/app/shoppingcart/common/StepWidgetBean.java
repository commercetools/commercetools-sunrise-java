package shoppingcart.common;

public enum StepWidgetBean {

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

    private boolean isStep(final StepWidgetBean what) {
        return name().equals(what.name());
    }
}
