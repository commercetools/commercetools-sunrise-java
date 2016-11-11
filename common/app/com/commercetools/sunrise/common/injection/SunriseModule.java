package com.commercetools.sunrise.common.injection;

import com.commercetools.sunrise.common.contexts.RequestContext;
import com.commercetools.sunrise.common.contexts.RequestContextProvider;
import com.commercetools.sunrise.common.contexts.RequestScope;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.*;
import com.commercetools.sunrise.common.pages.RoutesMultiControllerComponentResolverProvider;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.RequestHookContextImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Names;
import io.sphere.sdk.client.SphereAccessTokenSupplier;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereClientConfig;
import io.sphere.sdk.client.SphereClientFactory;
import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.utils.MoneyImpl;
import play.mvc.Http;

import javax.inject.Singleton;
import javax.money.Monetary;
import javax.money.format.MonetaryFormats;

public class SunriseModule extends AbstractModule {

    @Override
    protected void configure() {
        applyJavaMoneyHack();
        bindScope(RequestScoped.class, new RequestScope());
        bind(HttpClient.class).toInstance(SphereClientFactory.of().createHttpClient());
        bind(SphereClientConfig.class).toProvider(SphereClientConfigProvider.class).in(Singleton.class);
        bind(SphereAccessTokenSupplier.class).toProvider(SphereAccessTokenSupplierProvider.class).in(Singleton.class);
        bind(SphereClient.class).annotatedWith(Names.named("global")).toProvider(SphereClientProvider.class).in(Singleton.class);
        bind(ProductDataConfig.class).toProvider(ProductDataConfigProvider.class).in(Singleton.class);
        bind(RequestContext.class).toProvider(RequestContextProvider.class).in(RequestScoped.class);
        bind(RequestHookContext.class).to(RequestHookContextImpl.class).in(RequestScoped.class);
        bind(MultiControllerComponentResolver.class)
                .annotatedWith(Names.named("controllers"))
                .toProvider(RoutesMultiControllerComponentResolverProvider.class)
                .in(Singleton.class);
    }

    @Provides
    @RequestScoped
    public Http.Context httpContext() {
        return Http.Context.current();
    }

    @Provides
    @RequestScoped
    public Http.Session httpSession() {
        return httpContext().session();
    }

    private void applyJavaMoneyHack() {
        //fixes https://github.com/commercetools/commercetools-sunrise-java/issues/404
        //exception play.api.http.HttpErrorHandlerExceptions$$anon$1: Execution exception[[CompletionException: java.lang.IllegalArgumentException: java.util.concurrent.CompletionException: io.sphere.sdk.json.JsonException: detailMessage: com.fasterxml.jackson.databind.JsonMappingException: Operator failed: javax.money.DefaultMonetaryRoundingsSingletonSpi$DefaultCurrencyRounding@1655879e (through reference chain: io.sphere.sdk.payments.PaymentDraftImpl["amountPlanned"])
        Monetary.getDefaultRounding();
        Monetary.getDefaultRounding().apply(MoneyImpl.ofCents(123, "EUR"));
        Monetary.getDefaultAmountType();
        MonetaryFormats.getDefaultFormatProviderChain();
        Monetary.getDefaultCurrencyProviderChain();
    }
}
