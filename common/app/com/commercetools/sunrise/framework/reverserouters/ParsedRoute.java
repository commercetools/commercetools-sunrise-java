package com.commercetools.sunrise.framework.reverserouters;

import com.commercetools.sunrise.framework.SunriseModel;
import play.routing.Router;

import javax.annotation.Nullable;

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

    public static ParsedRoute of(final Router.RouteDocumentation routeDocumentation, @Nullable final Class<?> controllerClass) {
        return new ParsedRoute(routeDocumentation, controllerClass);
    }
}