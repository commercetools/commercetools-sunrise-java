package com.commercetools.sunrise.common.localization.changelanguage;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;

import java.util.function.Consumer;

@ImplementedBy(DefaultChangeLanguageControllerAction.class)
@FunctionalInterface
public interface ChangeLanguageControllerAction extends ControllerAction, Consumer<ChangeLanguageFormData> {

    @Override
    void accept(ChangeLanguageFormData formData);
}
