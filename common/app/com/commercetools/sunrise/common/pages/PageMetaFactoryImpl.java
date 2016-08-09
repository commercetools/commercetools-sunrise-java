package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.reverserouter.*;
import com.commercetools.sunrise.myaccount.CustomerSessionHandler;
import com.commercetools.sunrise.myaccount.UserBean;
import com.google.inject.Injector;
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
    @Inject
    private MyPersonalDetailsReverseRouter myPersonalDetailsReverseRouter;
    @Inject
    private MyOrdersReverseRouter myOrdersReverseRouter;
    @Inject
    private CartReverseRouter cartReverseRouter;
    @Inject
    private Injector injector;

    @Override
    public PageMeta create() {
        return getPageMeta(userContext, httpContext);
    }

    private PageMeta getPageMeta(final UserContext userContext, final Http.Context ctx) {
        final PageMeta pageMeta = new PageMeta();
        fillUser(pageMeta, ctx);
        pageMeta.setAssetsPath(reverseRouter.themeAssets("").url());
        pageMeta.setBagQuantityOptions(IntStream.rangeClosed(1, 9).boxed().collect(toList()));
        pageMeta.setCsrfToken(getCsrfToken(ctx.session()));
        final String language = userContext.locale().getLanguage();
        pageMeta.addHalLink(homeReverseRouter.homePageCall(language), "home", "continueShopping")
                .addHalLink(reverseRouter.processSearchProductsForm(language), "search")
                .addHalLink(reverseRouter.processChangeLanguageForm(), "selectLanguage")
                .addHalLink(reverseRouter.processChangeCountryForm(language), "selectCountry")

                .addHalLink(cartReverseRouter.showCart(language), "cart")
                .addHalLink(cartReverseRouter.processAddProductToCartForm(language), "addToCart")
                .addHalLink(cartReverseRouter.processChangeLineItemQuantityForm(language), "changeLineItem")
                .addHalLink(cartReverseRouter.processDeleteLineItemForm(language), "deleteLineItem")

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

                .addHalLink(myPersonalDetailsReverseRouter.myPersonalDetailsPageCall(language), "myPersonalDetails", "myAccount")
                .addHalLink(myPersonalDetailsReverseRouter.myPersonalDetailsProcessFormCall(language), "editMyPersonalDetails")
                .addHalLink(addressBookReverseRouter.addressBookCall(language), "myAddressBook")
                .addHalLink(addressBookReverseRouter.addAddressToAddressBookCall(language), "myAddressBookAddAddress")
                .addHalLink(addressBookReverseRouter.addAddressToAddressBookProcessFormCall(language), "myAddressBookAddAddressSubmit")
                .addHalLink(myOrdersReverseRouter.myOrderListPageCall(language), "myOrders")

                .addHalLinkOfHrefAndRel(ctx.request().uri(), "self");
        //TODO framework migration
//        newCategory().flatMap(nc -> reverseRouter.showCategory(userContext.locale(), nc))
//                .ifPresent(call -> pageMeta.addHalLink(call, "newProducts"));
//        pageMeta.setShowInfoModal(showInfoModal);
        return pageMeta;
    }

    private void fillUser(final PageMeta pageMeta, final Http.Context ctx) {
        final Http.Session session = ctx.session();
        final CustomerSessionHandler customerSessionHandler = injector.getInstance(CustomerSessionHandler.class);
        final UserBean bean = new UserBean();
        bean.setLoggedIn(customerSessionHandler.findCustomerId(session).isPresent());
        customerSessionHandler.findCustomerName(session).ifPresent(bean::setName);
        pageMeta.setUser(bean);
    }

}
