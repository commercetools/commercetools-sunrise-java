package com.commercetools.sunrise.shoppingcart.checkout;

public class DefaultPlaceOrderFormData implements PlaceOrderFormData {

    //@Constraints.Required
    //private boolean agreeTerms;
    //private boolean subscribeNewsletter;

    public DefaultPlaceOrderFormData() {
    }

    public String validate() {
// TODO Enable back once theme is adapted
//        if (!agreeTerms) {
//            return "You must agree to terms"; // TODO use i18n version
//        }
        return null;
    }
}
