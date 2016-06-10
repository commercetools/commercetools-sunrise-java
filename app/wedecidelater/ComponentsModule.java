package wedecidelater;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import framework.MultiControllerComponentResolver;
import framework.MultiControllerComponentResolverBuilder;
import shoppingcart.checkout.CheckoutCommonComponent;
import wedecidelatercommon.DefaultNavMenuControllerComponent;
import wedecidelatercommon.LocationSelectorControllerComponent;
import wedecidelatercommon.MiniCartControllerComponent;

public class ComponentsModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    public MultiControllerComponentResolver foo() {
        //here are also instanceof checks possible
        return new MultiControllerComponentResolverBuilder()
                .add(CheckoutCommonComponent.class, controller -> controller.getFrameworkTags().contains("checkout"))
                .add(MiniCartControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(DefaultNavMenuControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .add(LocationSelectorControllerComponent.class, controller -> !controller.getFrameworkTags().contains("checkout"))
                .build();
    }
}
