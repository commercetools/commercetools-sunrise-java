package framework;

import common.controllers.SunriseFrameworkController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class MultiControllerSunriseComponentResolverBuilder {
    private final Map<Class<? extends ControllerSunriseComponent>, Predicate<SunriseFrameworkController>> classToPredicateMap = new HashMap<>();

    public MultiControllerSunriseComponentResolverBuilder() {
    }

    public MultiControllerSunriseComponentResolverBuilder add(final Class<? extends ControllerSunriseComponent> clazz, final Predicate<SunriseFrameworkController> predicate) {
        classToPredicateMap.put(clazz, predicate);
        return this;
    }

    public MultiControllerSunriseComponentResolver build() {
        return new MultiControllerSunriseComponentResolverImpl(classToPredicateMap);
    }
}
