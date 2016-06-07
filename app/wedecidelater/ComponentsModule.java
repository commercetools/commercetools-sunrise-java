package wedecidelater;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import common.controllers.SunriseFrameworkController;
import framework.MultiControllerComponentResolver;
import framework.MultiControllerComponentResolverBuilder;
import shoppingcart.checkout.CheckoutCommonComponent;
import wedecidelatercommon.DefaultNavMenuControllerComponent;
import wedecidelatercommon.LocationSelectorControllerComponent;
import wedecidelatercommon.MiniCartControllerComponent;

import java.util.function.Predicate;

public class ComponentsModule extends AbstractModule {

    private static final Predicate<SunriseFrameworkController> CHECKOUT_PREDICATE = controller -> controller.getFrameworkTags().contains("checkout");
    private static final Predicate<SunriseFrameworkController> NO_CHECKOUT_PREDICATE = controller -> !controller.getFrameworkTags().contains("checkout");

    @Override
    protected void configure() {
    }

    @Provides
    public MultiControllerComponentResolver foo() {
        return new MultiControllerComponentResolverBuilder()
                .add(CheckoutCommonComponent.class, CHECKOUT_PREDICATE)
                .add(MiniCartControllerComponent.class, NO_CHECKOUT_PREDICATE)
                .add(DefaultNavMenuControllerComponent.class, NO_CHECKOUT_PREDICATE)
                .add(LocationSelectorControllerComponent.class, NO_CHECKOUT_PREDICATE)
                .build();
    }
}
