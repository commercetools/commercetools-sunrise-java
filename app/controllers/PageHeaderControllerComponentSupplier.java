package controllers;

import com.commercetools.sunrise.common.localization.LocationSelectorControllerComponent;
import com.commercetools.sunrise.common.pages.PageNavMenuControllerComponent;
import com.commercetools.sunrise.framework.components.AbstractControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.MiniCartControllerComponent;

import javax.inject.Inject;

public class PageHeaderControllerComponentSupplier extends AbstractControllerComponentSupplier {

    @Inject
    public PageHeaderControllerComponentSupplier(final MiniCartControllerComponent miniCartControllerComponent,
                                                 final PageNavMenuControllerComponent pageNavMenuControllerComponent,
                                                 final LocationSelectorControllerComponent locationSelectorControllerComponent) {
        add(miniCartControllerComponent);
        add(pageNavMenuControllerComponent);
        add(locationSelectorControllerComponent);
    }
}
