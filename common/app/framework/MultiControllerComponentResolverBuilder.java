package framework;

import common.controllers.SunriseFrameworkController;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public final class MultiControllerComponentResolverBuilder {
    private final Map<Class<? extends ControllerComponent>, Predicate<SunriseFrameworkController>> classToPredicateMap = new HashMap<>();

    public MultiControllerComponentResolverBuilder() {
    }

    public MultiControllerComponentResolverBuilder add(final Class<? extends ControllerComponent> clazz, final Predicate<SunriseFrameworkController> predicate) {
        classToPredicateMap.put(clazz, predicate);
        return this;
    }

    public MultiControllerComponentResolver build() {
        return new MultiControllerComponentResolverImpl(classToPredicateMap);
    }
}
