package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.framework.localization.ProjectContext;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.framework.template.i18n.I18nResolver;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.LocalizedStringEntry;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

@Singleton
final class PaymentSettingsImpl implements PaymentSettings {

    private static final String CONFIG_PAYMENT_KEY = "payment.settings";
    private static final String CONFIG_NAME_FIELD_KEY = "name";
    private static final String CONFIG_METHOD_FIELD_KEY = "method";
    private static final String CONFIG_PAYMENT_INTERFACE_FIELD_KEY = "paymentInterface";

    private final List<PaymentMethodInfo> paymentMethods;

    @Inject
    PaymentSettingsImpl(final Configuration configuration, final ProjectContext projectContext,
                        final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.paymentMethods = configuration.getConfigList(CONFIG_PAYMENT_KEY, emptyList()).stream()
                .map(paymentConfig -> {
                    final LocalizedString name = Optional.ofNullable(paymentConfig.getString(CONFIG_NAME_FIELD_KEY))
                            .map(i18nIdentifierFactory::create)
                            .map(i18nIdentifier -> buildLocalizedName(i18nIdentifier, i18nResolver, projectContext))
                            .orElse(null);
                    return PaymentMethodInfoBuilder.of()
                            .name(name)
                            .method(paymentConfig.getString(CONFIG_METHOD_FIELD_KEY))
                            .paymentInterface(paymentConfig.getString(CONFIG_PAYMENT_INTERFACE_FIELD_KEY))
                            .build();
                })
                .collect(toList());
    }

    @Override
    public CompletionStage<List<PaymentMethodInfo>> getPaymentMethods(final Cart cart) {
        return completedFuture(paymentMethods);
    }

    private static LocalizedString buildLocalizedName(final I18nIdentifier nameI18nIdentifier, final I18nResolver i18nResolver,
                                                      final ProjectContext projectContext) {
        return projectContext.locales().stream()
                .map(locale -> LocalizedStringEntry.of(locale, i18nResolver.getOrKey(singletonList(locale), nameI18nIdentifier)))
                .collect(LocalizedString.streamCollector());
    }
}
