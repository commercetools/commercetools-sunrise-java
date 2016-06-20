package demo;

import com.commercetools.sunrise.common.localization.LocationSelectorControllerComponent;
import com.commercetools.sunrise.common.pages.DefaultPageNavMenuControllerComponent;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolverBuilder;
import com.commercetools.sunrise.shoppingcart.MiniCartControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutCommonComponent;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import io.sphere.sdk.utils.MoneyImpl;

import javax.money.Monetary;
import javax.money.MonetaryRounding;

public class ComponentsModule extends AbstractModule {

    @Override
    protected void configure() {
        applyJavaMoneyHack();
    }

    private void applyJavaMoneyHack() {
        //fixes https://github.com/commercetools/commercetools-sunrise-java/issues/404
        //exception play.api.http.HttpErrorHandlerExceptions$$anon$1: Execution exception[[CompletionException: java.lang.IllegalArgumentException: java.util.concurrent.CompletionException: io.sphere.sdk.json.JsonException: detailMessage: com.fasterxml.jackson.databind.JsonMappingException: Operator failed: javax.money.DefaultMonetaryRoundingsSingletonSpi$DefaultCurrencyRounding@1655879e (through reference chain: io.sphere.sdk.payments.PaymentDraftImpl["amountPlanned"])
        Monetary.getDefaultRounding().apply(MoneyImpl.ofCents(123, "EUR"));
    }

    @Provides
    public MultiControllerComponentResolver foo() {
        //here are also instanceof checks possible
        return new MultiControllerComponentResolverBuilder()
                .add(CheckoutCommonComponent.class, controller -> controller.getFrameworkTags().contains("checkout"))
                .add(MiniCartControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(DefaultPageNavMenuControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(LocationSelectorControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .build();
    }
}
