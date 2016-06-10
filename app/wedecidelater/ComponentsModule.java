package wedecidelater;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.framework.MultiControllerComponentResolverBuilder;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutCommonComponent;
import com.commercetools.sunrise.wedecidelatercommon.DefaultNavMenuControllerComponent;
import com.commercetools.sunrise.wedecidelatercommon.LocationSelectorControllerComponent;
import com.commercetools.sunrise.wedecidelatercommon.MiniCartControllerComponent;

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
