package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.pages.*;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.RequestHook;
import com.commercetools.sunrise.hooks.SunrisePageDataHook;
import com.google.inject.Injector;
import io.sphere.sdk.client.SphereClient;
import play.libs.concurrent.HttpExecution;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SunriseFrameworkController extends Controller {
    private SphereClient sphere;

    @Inject
    private HookContext hookContext;

    @Inject
    private void setSphereClient(final SphereClient sphereClient) {
        sphere = new HookedSphereClient(sphereClient, this);
    }

    @Inject
    private UserContext userContext;
    @Inject
    private TemplateEngine templateEngine;
    @Inject
    private PageMetaFactory pageMetaFactory;
    @Inject
    private I18nResolver i18nResolver;

    protected SunriseFrameworkController() {
    }

    public abstract Set<String> getFrameworkTags();

    @Inject
    public void setMultiControllerComponents(final MultiControllerComponentResolver multiComponent, final Injector injector) {
        final List<Class<? extends ControllerComponent>> components = multiComponent.findMatchingComponents(this);
        components.forEach(clazz -> {
            final ControllerComponent instance = injector.getInstance(clazz);
            hooks().add(instance);
        });
    }

    protected final void registerControllerComponent(final ControllerComponent controllerComponent) {
        hooks().add(controllerComponent);
    }

    public SphereClient sphere() {
        return sphere;
    }

    public UserContext userContext() {
        return userContext;
    }

    public TemplateEngine templateEngine() {
        return templateEngine;
    }

    public I18nResolver i18nResolver() {
        return i18nResolver;
    }

    @Nullable
    public static String getCsrfToken(final Http.Session session) {
        return session.get("csrfToken");
    }

    protected CompletionStage<Html> renderPage(final PageContent pageContent, final String templateName) {
        final SunrisePageData pageData = createPageData(pageContent);
        return hooks().allAsyncHooksCompletionStage().thenApply(unused -> {
            hooks().runVoidHook(SunrisePageDataHook.class, hook -> hook.acceptSunrisePageData(pageData));
            final String html = templateEngine().render(templateName, pageData, userContext.locales());
            return new Html(html);
        });
    }

    protected final SunrisePageData createPageData(final PageContent pageContent) {
        final SunrisePageData pageData = new SunrisePageData();
        pageData.setHeader(new PageHeader(pageContent.getTitle()));
        pageData.setContent(pageContent);
        pageData.setFooter(new PageFooter());
        pageData.setMeta(pageMetaFactory.create());
        return pageData;
    }

    protected final CompletionStage<Result> doRequest(final Supplier<CompletionStage<Result>> nextSupplier) {
        final Function<RequestHook, CompletionStage<?>> f = hook -> hook.onRequest(ctx());
        hooks().runAsyncHook(RequestHook.class, f);
        return nextSupplier.get();
    }

    protected CompletionStage<Result> asyncOk(final CompletionStage<Html> htmlCompletionStage) {
        return htmlCompletionStage.thenApplyAsync(html -> ok(html), HttpExecution.defaultContext());
    }

    protected CompletionStage<Result> asyncBadRequest(final CompletionStage<Html> htmlCompletionStage) {
        return htmlCompletionStage.thenApplyAsync(html -> badRequest(html), HttpExecution.defaultContext());
    }

    protected void setI18nTitle(final PageContent pageContent, final String bundleWithKey) {
        pageContent.setTitle(i18nResolver.getOrEmpty(userContext().locales(), I18nIdentifier.of(bundleWithKey)));
    }

    protected final HookContext hooks() {
        return hookContext;
    }
}
