package com.commercetools.sunrise.framework.checkout.payment;

import com.commercetools.sunrise.contexts.ProjectContext;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.LocalizedStringEntry;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
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
    PaymentSettingsImpl(final Configuration configuration, final ProjectContext projectContext, final I18nIdentifierResolver i18nIdentifierResolver) {
        this.paymentMethods = configuration.getConfigList(CONFIG_PAYMENT_KEY, emptyList()).stream()
                .map(paymentConfig -> {
                    final String nameI18nKey = paymentConfig.getString(CONFIG_NAME_FIELD_KEY);
                    return PaymentMethodInfoBuilder.of()
                            .name(buildLocalizedName(nameI18nKey, projectContext.locales(), i18nIdentifierResolver))
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

    private static LocalizedString buildLocalizedName(final String nameI18nKey, final List<Locale> locales,
                                                      final I18nIdentifierResolver i18nIdentifierResolver) {
        return locales.stream()
                .map(locale -> LocalizedStringEntry.of(locale, i18nIdentifierResolver.resolveOrKey(nameI18nKey)))
                .collect(LocalizedString.streamCollector());
    }
}
