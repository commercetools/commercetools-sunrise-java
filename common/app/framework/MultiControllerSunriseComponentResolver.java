package framework;

import common.controllers.SunriseFrameworkController;

import java.util.List;

public interface MultiControllerSunriseComponentResolver {
    List<Class<? extends ControllerSunriseComponent>> findMatchingComponents(final SunriseFrameworkController controller);
}
