package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.framework.MultiControllerComponentResolver;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
final class RoutesMultiControllerComponentResolverProvider implements Provider<MultiControllerComponentResolver> {

    @Inject
    private ParsedRoutes parsedRoutes;

    @Override
    public MultiControllerComponentResolver get() {
        final List<Class<? extends ControllerComponent>> components = parsedRoutes.getRoutes().stream()
                .map(route -> route.getControllerClass())
                .filter(x -> x != null)
                .flatMap(this::findControllerComponentClasses)
                .distinct()
                .collect(Collectors.toList());
        return controller -> components;
    }

    protected Stream<? extends Class<? extends ControllerComponent>> findControllerComponentClasses(final Class<?> x) {
        final IntroducingMultiControllerComponents[] annotationsByType = x.getSuperclass().getAnnotationsByType(IntroducingMultiControllerComponents.class);
        return Arrays.stream(annotationsByType)
                .flatMap(sunriseControllerComponents -> Arrays.stream(sunriseControllerComponents.value()));
    }
}
