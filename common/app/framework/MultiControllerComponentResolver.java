package framework;

import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;

import java.util.List;

public interface MultiControllerComponentResolver {
    List<Class<? extends ControllerComponent>> findMatchingComponents(final SunriseFrameworkController controller);
}
