package com.commercetools.sunrise.common.localization.changecountry;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;

import java.util.function.Consumer;

@ImplementedBy(DefaultChangeCountryControllerAction.class)
@FunctionalInterface
public interface ChangeCountryControllerAction extends ControllerAction, Consumer<ChangeCountryFormData> {

    @Override
    void accept(ChangeCountryFormData formData);
}
