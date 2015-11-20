package purchase;

public class StepWidgetBean {
    private boolean shippingStepActive;
    private boolean paymentStepActive;

    public StepWidgetBean() {
    }

    public boolean isShippingStepActive() {
        return shippingStepActive;
    }

    public void setShippingStepActive(final boolean shippingStepActive) {
        this.shippingStepActive = shippingStepActive;
    }

    public boolean isPaymentStepActive() {
        return paymentStepActive;
    }

    public void setPaymentStepActive(final boolean paymentStepActive) {
        this.paymentStepActive = paymentStepActive;
    }
}
