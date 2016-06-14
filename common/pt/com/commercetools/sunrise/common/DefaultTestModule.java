package com.commercetools.sunrise.common;

import com.commercetools.sunrise.common.basicauth.BasicAuth;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.TestableSphereClient;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.pages.PageMetaFactory;
import com.commercetools.sunrise.common.template.cms.CmsService;
import com.commercetools.sunrise.common.template.engine.TemplateEngine;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
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
        bind(TemplateEngine.class).toInstance((t, p, l) -> "");
        bind(CmsService.class).toInstance((l, c) -> completedFuture(Optional.empty()));
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
}
