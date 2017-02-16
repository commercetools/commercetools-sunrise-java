package com.commercetools.sunrise.framework;

import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class RoutesMultiControllerComponentResolverProvider implements Provider<MultiControllerComponentResolver> {

    private final ParsedRouteList parsedRouteList;

    @Inject
    public RoutesMultiControllerComponentResolverProvider(final ParsedRouteList parsedRouteList) {
        this.parsedRouteList = parsedRouteList;
    }

    @Override
    public MultiControllerComponentResolver get() {
        final List<Class<? extends ControllerComponent>> components = parsedRouteList.getRoutes().stream()
                .map(ParsedRoute::getControllerClass)
                .filter(Objects::nonNull)
                .flatMap(this::findControllerComponentClasses)
                .distinct()
                .collect(Collectors.toList());
        return controller -> components;
    }

    private Stream<? extends Class<? extends ControllerComponent>> findControllerComponentClasses(final Class<?> x) {
        final IntroducingMultiControllerComponents[] annotationsByType = x.getSuperclass().getAnnotationsByType(IntroducingMultiControllerComponents.class);
        return Arrays.stream(annotationsByType)
                .flatMap(sunriseControllerComponents -> Arrays.stream(sunriseControllerComponents.value()));
    }
}
