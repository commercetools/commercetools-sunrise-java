package com.commercetools.sunrise.framework;

import com.commercetools.sunrise.common.controllers.SunriseController;

import java.util.List;

public interface MultiControllerComponentResolver {

    List<Class<? extends ControllerComponent>> findMatchingComponents(final SunriseController controller);
}
