package com.commercetools.sunrise.common;

import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.common.basicauth.BasicAuth;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.TestableSphereClient;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.pages.PageMetaFactory;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.framework.SunriseComponent;
import com.commercetools.sunrise.hooks.Hook;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.google.inject.AbstractModule;
import com.google.inject.util.Providers;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import play.routing.RoutingDsl;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(play.api.routing.Router.class).toInstance(new RoutingDsl().build().asScala()); // TODO Removes all routes to avoid having to resolve all injections while we have routes, remove later!
        bind(BasicAuth.class).toProvider(Providers.of(null));
        bind(SphereClient.class).toInstance(TestableSphereClient.ofEmptyResponse());
        bind(MultiControllerComponentResolver.class).toInstance(c -> emptyList());
        bind(PageMetaFactory.class).toInstance(PageMeta::new);
        bind(UserContext.class).toInstance(unsupportedUserContext());
        bind(I18nResolver.class).toInstance((l, i, h) -> Optional.empty());
        bind(TemplateEngine.class).toInstance((n, c) -> "");
        bind(CmsService.class).toInstance((l, c) -> completedFuture(Optional.empty()));
        bind(RequestHookContext.class).toInstance(unsupportedRequestHookContext());
    }

    private UserContext unsupportedUserContext() {
        return new UserContext() {
            @Override
            public CountryCode country() {
                return CountryCode.DE;
            }

            @Override
            public List<Locale> locales() {
                return singletonList(Locale.GERMANY);
            }

            @Override
            public CurrencyUnit currency() {
                return Monetary.getCurrency(Locale.GERMANY);
            }

            @Override
            public Optional<Reference<CustomerGroup>> customerGroup() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Optional<Reference<Channel>> channel() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private RequestHookContext unsupportedRequestHookContext() {
        return new RequestHookContext() {
            @Override
            public void add(final SunriseComponent component) {

            }

            @Override
            public <H extends Hook> CompletionStage<?> runEventHook(final Class<H> hookClass, final Function<H, CompletionStage<?>> f) {
                return completedFuture(null);
            }

            @Override
            public <H extends Hook, R> CompletionStage<R> runActionHook(final Class<H> hookClass, final BiFunction<H, R, CompletionStage<R>> f, final R param) {
                return completedFuture(param);
            }

            @Override
            public <H extends Hook, R> R runUnaryOperatorHook(final Class<H> hookClass, final BiFunction<H, R, R> f, final R param) {
                return param;
            }

            @Override
            public <H extends Hook> void runConsumerHook(final Class<H> hookClass, final Consumer<H> consumer) {

            }

            @Override
            public CompletionStage<Object> allAsyncHooksCompletionStage() {
                return completedFuture(null);
            }
        };
    }
}
