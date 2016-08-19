package com.commercetools.sunrise.common.injection;

import com.commercetools.sunrise.common.contexts.RequestScope;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.pages.RoutesMultiControllerComponentResolverProvider;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.sphere.sdk.utils.MoneyImpl;

import javax.inject.Singleton;
import javax.money.Monetary;
import javax.money.format.MonetaryFormats;

public class SunriseModule extends AbstractModule {

    @Override
    protected void configure() {
        applyJavaMoneyHack();
        bindScope(RequestScoped.class, new RequestScope());
        bind(MultiControllerComponentResolver.class)
                .annotatedWith(Names.named("controllers"))
                .toProvider(RoutesMultiControllerComponentResolverProvider.class)
                .in(Singleton.class);
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
