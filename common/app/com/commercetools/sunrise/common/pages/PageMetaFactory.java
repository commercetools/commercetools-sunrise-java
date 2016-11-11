package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.WebJarAssetsReverseRouter;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerInSession;
import com.google.inject.Injector;
import io.sphere.sdk.models.Base;
import play.filters.csrf.CSRF;
import play.mvc.Call;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class PageMetaFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private Http.Context httpContext;
    @Inject
    private Injector injector;

    public PageMeta create() {
        final PageMeta bean = new PageMeta();
        initialize(bean);
        return bean;
    }

    protected final void initialize(final PageMeta bean) {
        fillUserInfo(bean);
        fillAssetsPath(bean);
        fillBagQuantityOptions(bean);
        fillCsrfToken(bean);
        fillHomePageUrl(bean);
        fillSelfPageUrl(bean);
        fillNewProductsUrl(bean);
    }

    protected void fillCsrfToken(final PageMeta bean) {
        CSRF.getToken(httpContext.request())
                .map(CSRF.Token::value)
                .ifPresent(bean::setCsrfToken);
    }

    protected void fillBagQuantityOptions(final PageMeta bean) {
        bean.setBagQuantityOptions(IntStream
                .rangeClosed(1, 9)
                .boxed()
                .collect(toList()));
    }

    protected void fillUserInfo(final PageMeta bean) {
        injector.getInstance(CustomerInSession.class).findUserInfo()
                .ifPresent(bean::setUser);
    }

    protected void fillAssetsPath(final PageMeta bean) {
        final Call call = injector.getInstance(WebJarAssetsReverseRouter.class).webJarAssetsCall("");
        bean.setAssetsPath(call.url());
    }

    protected void fillHomePageUrl(final PageMeta bean) {
        final Call call = injector.getInstance(HomeReverseRouter.class).homePageCall(userContext.languageTag());
        bean.addHalLink(call, "home", "continueShopping");
    }

    protected void fillSelfPageUrl(final PageMeta bean) {
        bean.addHalLinkOfHrefAndRel(httpContext.request().uri(), "self");
    }

    protected void fillNewProductsUrl(final PageMeta bean) {
        //TODO framework migration
//        newCategory().flatMap(nc -> reverseRouter.showCategory(userContext.locale(), nc))
//                .ifPresent(call -> pageMeta.addHalLink(call, "newProducts"));
    }
}
