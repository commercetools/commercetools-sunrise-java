package demo.payments;

import com.commercetools.sunrise.payments.PaymentConfiguration;
import com.google.inject.AbstractModule;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.PaymentMethodInfoBuilder;

import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.singletonList;

public class PaymentsModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PaymentConfiguration.class).to(PaymentConfigImpl.class).in(Singleton.class);
    }

    private static class PaymentConfigImpl extends Base implements PaymentConfiguration {

        @Override
        public List<PaymentMethodInfo> getPaymentMethodInfoList() {
            return singletonList(PaymentMethodInfoBuilder.of()
                    .name(LocalizedString.of(Locale.ENGLISH, "Prepaid", Locale.GERMAN, "Vorkasse"))
                    .method("prepaid")
                    .build());
        }
    }
}
