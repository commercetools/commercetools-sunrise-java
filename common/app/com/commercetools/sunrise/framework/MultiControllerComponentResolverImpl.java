package com.commercetools.sunrise.framework;

import com.commercetools.sunrise.common.controllers.SunriseController;
import io.sphere.sdk.models.Base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class MultiControllerComponentResolverImpl extends Base implements MultiControllerComponentResolver {

    private final Map<Class<? extends ControllerComponent>, Predicate<SunriseController>> classToPredicateMap;

    public MultiControllerComponentResolverImpl(final Map<Class<? extends ControllerComponent>, Predicate<SunriseController>> classToPredicateMap) {
        this.classToPredicateMap = new HashMap<>(classToPredicateMap);
    }

    @Override
    public List<Class<? extends ControllerComponent>> findMatchingComponents(final SunriseController controller) {
        return classToPredicateMap.entrySet().stream()
                .filter(entry -> entry.getValue().test(controller))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
