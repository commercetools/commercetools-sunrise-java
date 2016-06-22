package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
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
    @Inject
    private HomeReverseRouter homeReverseRouter;
    @Inject
    private CheckoutReverseRouter checkoutReverseRouter;
    @Inject
    private AddressBookReverseRouter addressBookReverseRouter;

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
        pageMeta.addHalLink(homeReverseRouter.homePageCall(language), "home", "continueShopping")
                .addHalLink(reverseRouter.processSearchProductsForm(language), "search")
                .addHalLink(reverseRouter.processChangeLanguageForm(), "selectLanguage")
                .addHalLink(reverseRouter.processChangeCountryForm(language), "selectCountry")

                .addHalLink(reverseRouter.showCart(language), "cart")
                .addHalLink(reverseRouter.processAddProductToCartForm(language), "addToCart")
                .addHalLink(reverseRouter.processChangeLineItemQuantityForm(language), "changeLineItem")
                .addHalLink(reverseRouter.processDeleteLineItemForm(language), "deleteLineItem")

                .addHalLink(checkoutReverseRouter.checkoutAddressesPageCall(language), "checkout", "editShippingAddress", "editBillingAddress")
                .addHalLink(checkoutReverseRouter.checkoutAddressesProcessFormCall(language), "checkoutAddressSubmit")
                .addHalLink(checkoutReverseRouter.checkoutShippingPageCall(language), "editShippingMethod")
                .addHalLink(checkoutReverseRouter.checkoutShippingProcessFormCall(language), "checkoutShippingSubmit")
                .addHalLink(checkoutReverseRouter.checkoutPaymentPageCall(language), "editPaymentInfo")
                .addHalLink(checkoutReverseRouter.checkoutPaymentProcessFormCall(language), "checkoutPaymentSubmit")
                .addHalLink(checkoutReverseRouter.checkoutConfirmationProcessFormCall(language), "checkoutConfirmationSubmit")

                .addHalLink(reverseRouter.showLogInForm(language), "signIn", "logIn", "signUp")
                .addHalLink(reverseRouter.processLogInForm(language), "logInSubmit")
                .addHalLink(reverseRouter.processSignUpForm(language), "signUpSubmit")
                .addHalLink(reverseRouter.processLogOut(language), "logOut")

                .addHalLink(reverseRouter.showMyPersonalDetails(language), "myPersonalDetails", "myAccount")
                .addHalLink(reverseRouter.processMyPersonalDetailsForm(language), "editMyPersonalDetails")
                .addHalLink(addressBookReverseRouter.showMyAddressBook(language), "myAddressBook")
                .addHalLink(addressBookReverseRouter.showAddAddressToMyAddressBook(language), "myAddressBookAddAddress")
                .addHalLink(addressBookReverseRouter.processAddAddressToMyAddressBookForm(language), "myAddressBookAddAddressSubmit")
                .addHalLink(reverseRouter.showMyOrders(language), "myOrders")


                .addHalLink(reverseRouter.processMyPersonalDetailsForm(language), "editMyPersonalDetails")

                .addHalLinkOfHrefAndRel(ctx.request().uri(), "self");
        //TODO frameork migration
//        newCategory().flatMap(nc -> reverseRouter.showCategory(userContext.locale(), nc))
//                .ifPresent(call -> pageMeta.addHalLink(call, "newProducts"));
//        pageMeta.setShowInfoModal(showInfoModal);
        return pageMeta;
    }

}
