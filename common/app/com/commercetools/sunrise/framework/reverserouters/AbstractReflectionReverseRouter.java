package com.commercetools.sunrise.framework.reverserouters;

import io.sphere.sdk.models.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;

import static com.commercetools.sunrise.framework.reverserouters.ReflectionUtils.getClassByName;
import static java.util.stream.Collectors.toList;

public abstract class AbstractReflectionReverseRouter extends Base {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected final ReverseCaller getReverseCallerForSunriseRoute(final String sunriseRouteTag, final ParsedRoutes parsedRoutes) {
        final List<ReverseCaller> reverseCallers = findReverseCallersForSunriseRoute(sunriseRouteTag, parsedRoutes);
        if (reverseCallers.size() > 1) {
            logger.warn("Duplicated annotation @SunriseRoute {} in methods {}, selecting first match", sunriseRouteTag, reverseCallers);
        }
        return reverseCallers.stream()
                .findFirst()
                .orElseGet(() -> {
                    logger.warn("Cannot find annotation @SunriseRoute {} on any valid method, falling back to GET /", sunriseRouteTag);
                    return RootReverseCaller.INSTANCE;
                });
    }

    private List<ReverseCaller> findReverseCallersForSunriseRoute(final String sunriseRouteTag, final ParsedRoutes parsedRoutes) {
        return findMethodByName(sunriseRouteTag)
                .map(routerMethod -> parsedRoutes.getRoutes().stream()
                        .filter(parsedRoute -> parsedRoute.getControllerClass() != null)
                        .map(parsedRoute -> findReverseCallerForSunriseRoute(sunriseRouteTag, parsedRoute, routerMethod))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .distinct()
                        .collect(toList()))
                .orElseGet(Collections::emptyList);
    }

    private Optional<Method> findMethodByName(final String methodName) {
        return Arrays.stream(this.getClass().getMethods())
                .filter(method -> method.getName().equals(methodName))
                .findAny();
    }

    private Optional<ReverseCaller> findReverseCallerForSunriseRoute(final String sunriseRouteTag, final ParsedRoute parsedRoute, final Method routerMethod) {
        final Class<?> controllerClass = parsedRoute.getControllerClass();
        final Optional<Method> methodMatchingRouteOpt = findControllerMethodMatchingSunriseRoute(sunriseRouteTag, controllerClass);
        final Optional<Method> methodMatchingRouteAndParametersOpt = methodMatchingRouteOpt
                .filter(method -> Arrays.equals(method.getParameterTypes(), routerMethod.getParameterTypes()));
        if (methodMatchingRouteOpt.isPresent() && !methodMatchingRouteAndParametersOpt.isPresent()) {
            logger.warn("Annotation @SunriseRoute {} assigned to a method not matching parameters", sunriseRouteTag);
        }
        return methodMatchingRouteAndParametersOpt
                .flatMap(matchingMethod -> createReverseCaller(matchingMethod, controllerClass));
    }

    private Optional<Method> findControllerMethodMatchingSunriseRoute(final String sunriseRouteTag, final Class<?> controllerClass) {
        final List<Method> methods = Arrays.stream(controllerClass.getMethods())
                .filter(method -> matchesSunriseRoute(sunriseRouteTag, method))
                .collect(toList());
        if (methods.size() > 1) {
            logger.warn("Duplicated annotation @SunriseRoute {} in class {}, selecting first match", sunriseRouteTag, controllerClass.getCanonicalName());
        }
        return methods.stream().findFirst();
    }

    private boolean matchesSunriseRoute(final String sunriseRouteTag, final Method method) {
        return Optional.ofNullable(method.getDeclaredAnnotation(SunriseRoute.class))
                .map(annotationsByType -> Arrays.stream(annotationsByType.value())
                .anyMatch(sunriseRouteTag::equals))
                .orElse(false);
    }

    private Optional<ReverseCaller> createReverseCaller(final Method controllerMethod, final Class<?> controllerClass) {
        try {
            final String packageName = controllerClass.getPackage().getName();
            final Class<?> reverseRouter = getClassByName(packageName + ".routes");
            final Object o = reverseRouter.getField(controllerClass.getSimpleName()).get(null);
            final Method reverseRouteMethod = o.getClass().getMethod(controllerMethod.getName(), controllerMethod.getParameterTypes());
            return Optional.of(ReflectionReverseCaller.of(reverseRouteMethod, o));
        } catch (final NoSuchMethodException e) {
            return Optional.empty();
        } catch (final Exception e) {
            throw new CompletionException(e);
        }
    }
}
