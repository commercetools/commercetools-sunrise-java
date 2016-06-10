package com.commercetools.sunrise.common.inject.wedecidelater;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.PageMeta;
import com.commercetools.sunrise.common.controllers.PageMetaFactory;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerSessionUtils;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.stream.IntStream;

import static com.commercetools.sunrise.common.controllers.SunriseFrameworkController.getCsrfToken;
import static java.util.stream.Collectors.toList;

public class PageMetaFactoryImpl implements PageMetaFactory {
    @Inject
    private UserContext userContext;
    @Inject
    private Http.Context httpContext;
    @Inject
    private ReverseRouter reverseRouter;

    @Override
    public PageMeta create() {
        return getPageMeta(userContext, httpContext);
    }

    private PageMeta getPageMeta(final UserContext userContext, final Http.Context ctx) {
        final PageMeta pageMeta = new PageMeta();
        pageMeta.setUser(CustomerSessionUtils.getUserBean(ctx.session()));
        pageMeta.setAssetsPath(reverseRouter.themeAssets("").url());
        pageMeta.setBagQuantityOptions(IntStream.rangeClosed(1, 9).boxed().collect(toList()));
        pageMeta.setCsrfToken(getCsrfToken(ctx.session()));
        final String language = userContext.locale().getLanguage();
        pageMeta.addHalLink(reverseRouter.showHome(language), "home", "continueShopping")
                .addHalLink(reverseRouter.processSearchProductsForm(language), "search")
                .addHalLink(reverseRouter.processChangeLanguageForm(), "selectLanguage")
                .addHalLink(reverseRouter.processChangeCountryForm(language), "selectCountry")

                .addHalLink(reverseRouter.showCart(language), "cart")
                .addHalLink(reverseRouter.processAddProductToCartForm(language), "addToCart")
                .addHalLink(reverseRouter.processChangeLineItemQuantityForm(language), "changeLineItem")
                .addHalLink(reverseRouter.processDeleteLineItemForm(language), "deleteLineItem")

                .addHalLink(reverseRouter.showCheckoutAddressesForm(language), "checkout", "editShippingAddress", "editBillingAddress")
                .addHalLink(reverseRouter.processCheckoutAddressesForm(language), "checkoutAddressSubmit")
                .addHalLink(reverseRouter.showCheckoutShippingForm(language), "editShippingMethod")
                .addHalLink(reverseRouter.processCheckoutShippingForm(language), "checkoutShippingSubmit")
                .addHalLink(reverseRouter.showCheckoutPaymentForm(language), "editPaymentInfo")
                .addHalLink(reverseRouter.processCheckoutPaymentForm(language), "checkoutPaymentSubmit")
                .addHalLink(reverseRouter.processCheckoutConfirmationForm(language), "checkoutConfirmationSubmit")

                .addHalLink(reverseRouter.showLogInForm(language), "signIn", "logIn", "signUp")
                .addHalLink(reverseRouter.processLogInForm(language), "logInSubmit")
                .addHalLink(reverseRouter.processSignUpForm(language), "signUpSubmit")
                .addHalLink(reverseRouter.processLogOut(language), "logOut")

                .addHalLink(reverseRouter.processMyPersonalDetailsForm(language), "editMyPersonalDetails")

                .addHalLinkOfHrefAndRel(ctx.request().uri(), "self");
        //TODO frameork migration
//        newCategory().flatMap(nc -> reverseRouter.showCategory(userContext.locale(), nc))
//                .ifPresent(call -> pageMeta.addHalLink(call, "newProducts"));
//        pageMeta.setShowInfoModal(showInfoModal);
        return pageMeta;
    }

}
