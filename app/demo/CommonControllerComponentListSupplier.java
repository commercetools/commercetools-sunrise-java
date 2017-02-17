package demo;

import com.commercetools.sunrise.common.sessions.cart.CartInSessionControllerComponent;
import com.commercetools.sunrise.common.sessions.customer.CustomerInSessionControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponentListSupplier;
import com.commercetools.sunrise.shoppingcart.CartFieldsUpdaterControllerComponent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class CommonControllerComponentListSupplier implements ControllerComponentListSupplier {

    private final List<ControllerComponent> components = new ArrayList<>();

    @Inject
    public CommonControllerComponentListSupplier(final CartFieldsUpdaterControllerComponent cartFieldsUpdaterControllerComponent,
                                                 final CartInSessionControllerComponent cartInSessionControllerComponent,
                                                 final CustomerInSessionControllerComponent customerInSessionControllerComponent) {
        this.components.addAll(asList(cartFieldsUpdaterControllerComponent, cartInSessionControllerComponent, customerInSessionControllerComponent));
    }

    @Override
    public List<ControllerComponent> get() {
        return components;
    }
}
