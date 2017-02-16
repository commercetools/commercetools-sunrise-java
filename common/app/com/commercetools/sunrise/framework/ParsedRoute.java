package com.commercetools.sunrise.framework;

import com.commercetools.sunrise.common.models.SunriseModel;
import org.apache.commons.lang3.StringUtils;
import play.routing.Router;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionException;

import static com.commercetools.sunrise.common.utils.ReflectionUtils.getClassByName;
import static org.apache.commons.lang3.StringUtils.countMatches;

public final class ParsedRoute extends SunriseModel {

    private final Router.RouteDocumentation routeDocumentation;
    @Nullable
    private final Class<?> controllerClass;

    private ParsedRoute(final Router.RouteDocumentation routeDocumentation, @Nullable final Class<?> controllerClass) {
        this.routeDocumentation = routeDocumentation;
        this.controllerClass = controllerClass;
    }

    public Router.RouteDocumentation getRouteDocumentation() {
        return routeDocumentation;
    }

    @Nullable
    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public static ParsedRoute of(final Router.RouteDocumentation routeDocumentation) {
        final Class<?> clazz = findClass(routeDocumentation.getControllerMethodInvocation());
        return new ParsedRoute(routeDocumentation, clazz);
    }

    @Nullable
    private static Class<?> findClass(final String controllerMethodInvocation) {
        if (countMatches(controllerMethodInvocation, '@') == 2) {
            final String controllerMethod = StringUtils.removeStart(controllerMethodInvocation, "@");
            final String controllerClassName = controllerMethod.substring(0, controllerMethod.indexOf("@"));
            try {
                return getClassByName(controllerClassName);
            } catch (ClassNotFoundException e) {
                throw new CompletionException(e);
            }
        }
        return null;
    }
}