package common.pages;

import play.mvc.Call;

public interface ReverseRouter {

    Call category(final String language, final String slug, final int page);

    Call processCheckoutShippingForm(final String language);

    Call showCheckoutShippingForm(final String language);
}
