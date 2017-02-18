package demo;

import com.commercetools.sunrise.common.localization.LocationSelectorControllerComponent;
import com.commercetools.sunrise.common.pages.PageNavMenuControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.ControllerComponentSupplier;
import com.commercetools.sunrise.shoppingcart.MiniCartControllerComponent;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class PageHeaderControllerComponentSupplier implements ControllerComponentSupplier {

    private final List<ControllerComponent> components = new ArrayList<>();

    @Inject
    public PageHeaderControllerComponentSupplier(final MiniCartControllerComponent miniCartControllerComponent,
                                                 final PageNavMenuControllerComponent pageNavMenuControllerComponent,
                                                 final LocationSelectorControllerComponent locationSelectorControllerComponent) {
        this.components.addAll(asList(miniCartControllerComponent, pageNavMenuControllerComponent, locationSelectorControllerComponent));
    }

    @Override
    public List<ControllerComponent> get() {
        return components;
    }
}
