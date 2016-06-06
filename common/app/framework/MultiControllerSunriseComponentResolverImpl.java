package framework;

import common.controllers.SunriseFrameworkController;
import io.sphere.sdk.models.Base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class MultiControllerSunriseComponentResolverImpl extends Base implements MultiControllerSunriseComponentResolver {
    private final Map<Class<? extends ControllerSunriseComponent>, Predicate<SunriseFrameworkController>> classToPredicateMap;

    public MultiControllerSunriseComponentResolverImpl(final Map<Class<? extends ControllerSunriseComponent>, Predicate<SunriseFrameworkController>> classToPredicateMap) {
        this.classToPredicateMap = new HashMap<>(classToPredicateMap);
    }

    @Override
    public List<Class<? extends ControllerSunriseComponent>> findMatchingComponents(final SunriseFrameworkController controller) {
        return classToPredicateMap.entrySet().stream()
                .filter(entry -> entry.getValue().test(controller))
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
    }
}
