package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.controllers.WebJarAssetsReverseRouter;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.sessions.customer.CustomerInSession;
import play.filters.csrf.CSRF;
import play.mvc.Call;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class PageMetaFactory extends ViewModelFactory<PageMeta, PageContent> {

    private final Http.Context httpContext;
    private final CustomerInSession customerInSession;
    private final HomeReverseRouter homeReverseRouter;
    private final WebJarAssetsReverseRouter webJarAssetsReverseRouter;

    @Inject
    public PageMetaFactory(final Http.Context httpContext, final CustomerInSession customerInSession,
                           final HomeReverseRouter homeReverseRouter, final WebJarAssetsReverseRouter webJarAssetsReverseRouter) {
        this.httpContext = httpContext;
        this.customerInSession = customerInSession;
        this.homeReverseRouter = homeReverseRouter;
        this.webJarAssetsReverseRouter = webJarAssetsReverseRouter;
    }

    @Override
    protected PageMeta getViewModelInstance() {
        return new PageMeta();
    }

    public final PageMeta create(final PageContent content) {
        return initializedViewModel(content);
    }

    @Override
    protected final void initialize(final PageMeta model, final PageContent content) {
        fillUserInfo(model, content);
        fillAssetsPath(model, content);
        fillBagQuantityOptions(model, content);
        fillCsrfToken(model, content);
        fillHomePageUrl(model, content);
        fillSelfPageUrl(model, content);
    }

    protected void fillCsrfToken(final PageMeta model, final PageContent content) {
        CSRF.getToken(httpContext.request())
                .map(CSRF.Token::value)
                .ifPresent(model::setCsrfToken);
    }

    protected void fillBagQuantityOptions(final PageMeta model, final PageContent content) {
        model.setBagQuantityOptions(IntStream
                .rangeClosed(1, 9)
                .boxed()
                .collect(toList()));
    }

    protected void fillUserInfo(final PageMeta model, final PageContent content) {
        customerInSession.findUserInfo()
                .ifPresent(model::setUser);
    }

    protected void fillAssetsPath(final PageMeta model, final PageContent content) {
        final Call call = webJarAssetsReverseRouter.webJarAssetsCall("");
        model.setAssetsPath(call.url());
    }

    protected void fillHomePageUrl(final PageMeta model, final PageContent content) {
        final Call call = homeReverseRouter.homePageCall();
        model.addHalLink(call, "home", "continueShopping");
    }

    protected void fillSelfPageUrl(final PageMeta model, final PageContent content) {
        model.addHalLinkOfHrefAndRel(httpContext.request().uri(), "self");
    }
}
