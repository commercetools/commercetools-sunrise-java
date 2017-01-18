package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WebJarAssetsReverseRouter;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerInSession;
import play.filters.csrf.CSRF;
import play.mvc.Call;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Locale;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class PageMetaFactory extends ViewModelFactory {

    private final Locale locale;
    private final Http.Context httpContext;
    private final CustomerInSession customerInSession;
    private final HomeReverseRouter homeReverseRouter;
    private final WebJarAssetsReverseRouter webJarAssetsReverseRouter;

    @Inject
    public PageMetaFactory(final Locale locale, final Http.Context httpContext, final CustomerInSession customerInSession,
                           final HomeReverseRouter homeReverseRouter, final WebJarAssetsReverseRouter webJarAssetsReverseRouter) {
        this.locale = locale;
        this.httpContext = httpContext;
        this.customerInSession = customerInSession;
        this.homeReverseRouter = homeReverseRouter;
        this.webJarAssetsReverseRouter = webJarAssetsReverseRouter;
    }

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
        customerInSession.findUserInfo()
                .ifPresent(bean::setUser);
    }

    protected void fillAssetsPath(final PageMeta bean) {
        final Call call = webJarAssetsReverseRouter.webJarAssetsCall("");
        bean.setAssetsPath(call.url());
    }

    protected void fillHomePageUrl(final PageMeta bean) {
        final Call call = homeReverseRouter.homePageCall(locale.toLanguageTag());
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
