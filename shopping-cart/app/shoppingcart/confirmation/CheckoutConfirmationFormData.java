package shoppingcart.confirmation;

public class CheckoutConfirmationFormData {
    private boolean subscribeNewsletter;
    private boolean agreeTerms;

    public CheckoutConfirmationFormData() {
    }

    public boolean isAgreeTerms() {
        return agreeTerms;
    }

    public void setAgreeTerms(final boolean agreeTerms) {
        this.agreeTerms = agreeTerms;
    }

    public boolean isSubscribeNewsletter() {
        return subscribeNewsletter;
    }

    public void setSubscribeNewsletter(final boolean subscribeNewsletter) {
        this.subscribeNewsletter = subscribeNewsletter;
    }
}
