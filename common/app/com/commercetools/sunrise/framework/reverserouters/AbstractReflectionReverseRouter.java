package com.commercetools.sunrise.framework.reverserouters;

import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;

import static com.commercetools.sunrise.common.utils.ReflectionUtils.getClassByName;
import static java.util.stream.Collectors.toList;

public abstract class AbstractReflectionReverseRouter extends Base {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final ReverseCaller getCallerForRoute(final ParsedRouteList parsedRouteList, final String routeTag) {
        return findMethodWithName(routeTag)
                .flatMap(routerMethod -> {
                    final List<Pair<Object, Method>> pairs = parsedRouteList.getRoutes().stream()
                            .filter(route -> route.getControllerClass() != null)
                            .map(route -> findReverseRouterMethod(route, routeTag, routerMethod))
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .distinct()
                            .collect(toList());
                    if (pairs.size() > 1) {
                        logger.warn("Duplicated annotation @SunriseRoute {} in methods {}, selecting first match",
                                routeTag,
                                pairs.stream().map(route -> route.getRight().getName()).collect(toList()));
                    }
                    return pairs.stream()
                            .map(p -> (ReverseCaller) ReflectionReverseCaller.of(p.getRight(), p.getLeft()))
                            .findFirst();
                })
                .orElseGet(() -> {
                    logger.warn("Cannot find annotation @SunriseRoute {} on any valid method, falling back to GET /", routeTag);
                    return RootReverseCaller.INSTANCE;
                });
    }

    private Optional<Method> findMethodWithName(final String methodName) {
        return Arrays.stream(this.getClass().getMethods())
                .filter(method -> method.getName().equals(methodName))
                .findAny();
    }

    private Optional<Pair<Object, Method>> findReverseRouterMethod(final ParsedRoute parsedRoute, final String routeTag, final Method routerMethod) {
        final Class<?> controllerClass = parsedRoute.getControllerClass();
        final Optional<Method> methodMatchingRouteOpt = findControllerMethodMatchingRoute(controllerClass, routeTag);
        final Optional<Method> methodMatchingRouteAndParametersOpt = methodMatchingRouteOpt
                .filter(method -> Arrays.equals(method.getParameterTypes(), routerMethod.getParameterTypes()));
        if (methodMatchingRouteOpt.isPresent() && !methodMatchingRouteAndParametersOpt.isPresent()) {
            logger.warn("Annotation @SunriseRoute {} assigned to a method not matching parameters", routeTag);
        }
        return methodMatchingRouteAndParametersOpt.flatMap(m -> createPair(m, controllerClass));
    }

    private Optional<Method> findControllerMethodMatchingRoute(final Class<?> controllerClass, final String routeTag) {
        final List<Method> methods = Arrays.stream(controllerClass.getMethods())
                .filter(method -> matchesSunriseRoute(method, routeTag))
                .collect(toList());
        if (methods.size() > 1) {
            logger.warn("Duplicated annotation @SunriseRoute {} in class {}, selecting first match", routeTag, controllerClass.getCanonicalName());
        }
        return methods.stream().findFirst();
    }

    private boolean matchesSunriseRoute(final Method method, final String routeTag) {
        return Optional.ofNullable(method.getDeclaredAnnotation(SunriseRoute.class))
                .map(annotationsByType -> Arrays.stream(annotationsByType.value())
                .anyMatch(routeTag::equals))
                .orElse(false);
    }

    private Optional<Pair<Object, Method>> createPair(final Method controllerMethod, final Class<?> controllerClass) {
        try {
            final String packageName = controllerClass.getPackage().getName();
            final Class<?> reverseRouter = getClassByName(packageName + ".routes");
            final Object o = reverseRouter.getField(controllerClass.getSimpleName()).get(null);
            final Method reverseRouteMethod = o.getClass().getMethod(controllerMethod.getName(), controllerMethod.getParameterTypes());
            return Optional.of(ImmutablePair.of(o, reverseRouteMethod));
        } catch (final NoSuchMethodException e) {
            return Optional.empty();
        } catch (final Exception e) {
            throw new CompletionException(e);
        }
    }
}
