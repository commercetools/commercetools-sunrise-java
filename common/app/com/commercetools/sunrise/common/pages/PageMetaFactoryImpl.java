package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.reverserouter.*;
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
    private AuthenticationReverseRouter authenticationReverseRouter;

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
                .addHalLink(reverseRouter.processChangeLanguageForm(), "selectLanguage")
                .addHalLink(reverseRouter.processChangeCountryForm(language), "selectCountry")
                .addHalLinkOfHrefAndRel(ctx.request().uri(), "self");
        //TODO framework migration
//        newCategory().flatMap(nc -> reverseRouter.showCategory(userContext.locale(), nc))
//                .ifPresent(call -> pageMeta.addHalLink(call, "newProducts"));
//        pageMeta.setShowInfoModal(showInfoModal);
        return pageMeta;
    }

}
