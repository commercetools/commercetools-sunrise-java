package com.commercetools.sunrise.framework.reverserouters;

import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionException;

import static com.commercetools.sunrise.common.utils.ReflectionUtils.getClassByName;
import static java.lang.String.format;

public abstract class AbstractReflectionReverseRouter extends Base {

    protected final ReverseCaller getCallerForRoute(final ParsedRouteList parsedRouteList, final String tag) {
        try {
            return parsedRouteList.getRoutes().stream()
                    .filter(r -> r.getControllerClass() != null)
                    .map((parsedRoute) -> findReverseRouterMethod(parsedRoute, tag))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(p -> (ReverseCaller) ReflectionReverseCaller.of(p.getRight(), p.getLeft()))
                    .findFirst()
                    .orElseGet(() -> {
                        LoggerFactory.getLogger(this.getClass()).warn(format("Cannot find route for %s, falling back to GET /.", tag));
                        return RootReverseCaller.INSTANCE;
                    });
        } catch (final Exception e) {
            throw new CompletionException(e);
        }
    }

    private Optional<Pair<Object, Method>> findReverseRouterMethod(final ParsedRoute parsedRoute, final String tag) {
        final Class<?> controllerClass = parsedRoute.getControllerClass();
        Optional<Method> controllerMethodOptional = findControllerMethod(controllerClass, tag);
        return controllerMethodOptional.flatMap(m -> createPair(m, controllerClass));
    }

    private Optional<Pair<Object, Method>> createPair(final Method controllerMethod, final Class<?> controllerClass) {
        try {
            final String packageName = controllerClass.getPackage().getName();
            final Class<?> reverseRouter = getClassByName(packageName + ".routes");
            final Field field = reverseRouter.getField(controllerClass.getSimpleName());
            final Object o = field.get(null);
            final Method reverseRouteMethod = o.getClass().getMethod(controllerMethod.getName(), controllerMethod.getParameterTypes());
            return Optional.of(ImmutablePair.of(o, reverseRouteMethod));
        } catch (final NoSuchMethodException e) {
            return Optional.empty();
        } catch (final Exception e) {
            throw new CompletionException(e);
        }
    }

    private Optional<Method> findControllerMethod(final Class<?> controllerClass, final String tag) {
        try {
            return Arrays.stream(controllerClass.getMethods())
                    .filter(method -> methodFilter(method, tag))
                    .findFirst();
        } catch (final Exception e) {
            throw new CompletionException("failed in findControllerMethod", e);
        }
    }

    private boolean methodFilter(final Method method, final String tag) {
        try {
            final SunriseRoute annotationsByTypeOption = method.getDeclaredAnnotation(SunriseRoute.class);
            return Optional.ofNullable(annotationsByTypeOption)
                    .map(annotationsByType -> Arrays.stream(annotationsByType.value())
                    .anyMatch(tag::equals))
                    .orElse(false);
        } catch (final Exception e) {
            throw new CompletionException("failed in methodFilter", e);
        }
    }
}
