package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.hooks.Hook;
import com.commercetools.sunrise.hooks.HookRunner;
import com.commercetools.sunrise.hooks.RequestHook;
import com.commercetools.sunrise.common.pages.*;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.hooks.SunrisePageDataHook;
import com.google.inject.Injector;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.utils.FutureUtils;
import play.libs.concurrent.HttpExecution;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public abstract class SunriseFrameworkController extends Controller implements HookRunner {

    @Inject
    private SphereClient sphere;
    @Inject
    private UserContext userContext;
    @Inject
    private TemplateEngine templateEngine;
    @Inject
    private PageMetaFactory pageMetaFactory;
    @Inject
    private I18nResolver i18nResolver;

    private final List<ControllerComponent> controllerComponents = new LinkedList<>();
    private final List<CompletionStage<Object>> asyncHooksCompletionStages = new LinkedList<>();

    protected SunriseFrameworkController() {
    }

    public abstract Set<String> getFrameworkTags();

    @Inject
    public void setMultiControllerComponents(final MultiControllerComponentResolver multiComponent, final Injector injector) {
        final List<Class<? extends ControllerComponent>> components = multiComponent.findMatchingComponents(this);
        components.forEach(clazz -> {
            final ControllerComponent instance = injector.getInstance(clazz);
            controllerComponents.add(instance);
        });
    }

    protected final void registerControllerComponent(final ControllerComponent controllerComponent) {
        controllerComponents.add(controllerComponent);
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
        return allAsyncHooksCompletionStage().thenApply(unused -> {
            runVoidHook(SunrisePageDataHook.class, hook -> hook.acceptSunrisePageData(pageData));
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
        runAsyncHook(RequestHook.class, hook -> hook.onRequest(ctx()));
        return nextSupplier.get();
    }

    public final <T extends Hook> void runVoidHook(final Class<T> hookClass, final Consumer<T> consumer) {
        controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .forEach(action -> consumer.accept((T) action));
    }

    @Override
    public final <T extends Hook, R> R runFilterHook(final Class<T> hookClass, final BiFunction<T, R, R> f, final R param) {
        R result = param;
        final List<T> applicableHooks = controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .map(x -> (T) x)
                .collect(Collectors.toList());
        for (final T hook : applicableHooks) {
            result = f.apply(hook, result);
        }
        return result;
    }

    @Override
    public final <T extends Hook> CompletionStage<Object> runAsyncHook(final Class<T> hookClass, final Function<T, CompletionStage<?>> f) {
        //TODO throw a helpful NPE if component returns null instead of CompletionStage
        final List<CompletionStage<Void>> collect = controllerComponents.stream()
                .filter(x -> hookClass.isAssignableFrom(x.getClass()))
                .map(hook -> f.apply((T) hook))
                .map(stage -> (CompletionStage<Void>) stage)
                .collect(toList());
        final CompletionStage<?> listCompletionStage = FutureUtils.listOfFuturesToFutureOfList(collect);
        final CompletionStage<Object> result = listCompletionStage.thenApply(z -> null);
        asyncHooksCompletionStages.add(result);
        return result;
    }

    protected final CompletionStage<Object> allAsyncHooksCompletionStage() {
        return FutureUtils.listOfFuturesToFutureOfList(asyncHooksCompletionStages).thenApply(list -> null);
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
}
