package com.commercetools.sunrise.framework.checkout.confirmation;

public class DefaultCheckoutConfirmationFormData implements CheckoutConfirmationFormData {

    //@Constraints.Required
    //private boolean agreeTerms;
    //private boolean subscribeNewsletter;

    public DefaultCheckoutConfirmationFormData() {
    }

    public String validate() {
// TODO Enable back once theme is adapted
//        if (!agreeTerms) {
//            return "You must agree to terms"; // TODO use i18n version
//        }
        return null;
    }

//    public boolean isAgreeTerms() {
//        return agreeTerms;
//    }
//
//    public void setAgreeTerms(final boolean agreeTerms) {
//        this.agreeTerms = agreeTerms;
//    }
//
//    public boolean isSubscribeNewsletter() {
//        return subscribeNewsletter;
//    }
//
//    public void setSubscribeNewsletter(final boolean subscribeNewsletter) {
//        this.subscribeNewsletter = subscribeNewsletter;
//    }
}
