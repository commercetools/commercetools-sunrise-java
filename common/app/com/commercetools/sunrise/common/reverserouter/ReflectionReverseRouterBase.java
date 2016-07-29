package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoute;
import com.commercetools.sunrise.common.pages.ParsedRoutes;
import com.commercetools.sunrise.framework.annotations.ReverseRoute;
import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletionException;

abstract class ReflectionReverseRouterBase extends Base {


    protected final ReverseCaller getCallerForRoute(final ParsedRoutes parsedRoutes, final String tag) {
        try {
            final ReverseCaller reverseCaller = parsedRoutes.getRoutes().stream()
                    .filter(r -> r.getControllerClass() != null)
                    .map((parsedRoute) -> getParsedRouteRFunction(parsedRoute, tag))
                    .filter(Optional::isPresent)
                    .map(x -> x.get())
                    .map(p -> new ReverseCaller(p.getRight(), p.getLeft()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("cannot find method for tag " + tag));
            return reverseCaller;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Pair<Object, Method>> getParsedRouteRFunction(final ParsedRoute parsedRoute, final String tag) {
        final Class<?> controllerClass = parsedRoute.getControllerClass();
        Optional<Method> controllerMethodOptional = foo(controllerClass, tag);
        return controllerMethodOptional.map(m -> createPair(m, controllerClass));
    }

    private Pair<Object, Method> createPair(final Method controllerMethod, final Class<?> controllerClass) {
        try {
            final String packageName = controllerClass.getPackage().getName();
            final Class<?> reverseRouter = Class.forName(packageName + ".routes");
            final Field field = reverseRouter.getField(controllerClass.getSimpleName());
            final Object o = field.get(null);
            final Method reverseRouteMethod = o.getClass().getMethod(controllerMethod.getName(), controllerMethod.getParameterTypes());
            return ImmutablePair.of(o, reverseRouteMethod);
        } catch (Exception e) {
            throw new CompletionException(e);
        }
    }

    private Optional<Method> foo(final Class<?> controllerClass, final String tag) {
        try {
            return Arrays.stream(controllerClass.getMethods())
                    .filter(method -> methodFilter(method, tag)).findFirst();
        } catch (final Exception e) {
            throw new CompletionException("failed in foo", e);
        }
    }

    private boolean methodFilter(final Method method, final String tag) {
        try {
            if (method.getName().equals("changeLanguage")) {
                System.err.println("here");
            }
            final ReverseRoute annotationsByTypeOption = method.getDeclaredAnnotation(ReverseRoute.class);
            return Optional.ofNullable(annotationsByTypeOption).map(annotationsByType -> Arrays.stream(annotationsByType.value())
                    .anyMatch(value -> tag.equals(value))).orElse(false);
        } catch (final Exception e) {
            throw new CompletionException("failed in methodFilter", e);
        }
    }
}
