package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.WebJarAssetsReverseRouter;
import com.commercetools.sunrise.common.models.CommonViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.HomeSimpleReverseRouter;
import com.commercetools.sunrise.myaccount.CustomerInSession;
import play.filters.csrf.CSRF;
import play.mvc.Call;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Locale;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class PageMetaFactory extends CommonViewModelFactory<PageMeta> {

    private final Locale locale;
    private final Http.Context httpContext;
    private final CustomerInSession customerInSession;
    private final HomeSimpleReverseRouter homeReverseRouter;
    private final WebJarAssetsReverseRouter webJarAssetsReverseRouter;

    @Inject
    public PageMetaFactory(final Locale locale, final Http.Context httpContext, final CustomerInSession customerInSession,
                           final HomeSimpleReverseRouter homeReverseRouter, final WebJarAssetsReverseRouter webJarAssetsReverseRouter) {
        this.locale = locale;
        this.httpContext = httpContext;
        this.customerInSession = customerInSession;
        this.homeReverseRouter = homeReverseRouter;
        this.webJarAssetsReverseRouter = webJarAssetsReverseRouter;
    }

    public final PageMeta create() {
        return initializedViewModel();
    }

    @Override
    protected PageMeta getViewModelInstance() {
        return new PageMeta();
    }

    @Override
    protected final void initialize(final PageMeta model) {
        fillUserInfo(model);
        fillAssetsPath(model);
        fillBagQuantityOptions(model);
        fillCsrfToken(model);
        fillHomePageUrl(model);
        fillSelfPageUrl(model);
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
}
