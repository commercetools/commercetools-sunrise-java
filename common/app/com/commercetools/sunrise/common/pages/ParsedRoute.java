package com.commercetools.sunrise.common.pages;

import io.sphere.sdk.models.Base;
import play.routing.Router;

public final class ParsedRoute extends Base {
    private Router.RouteDocumentation routeDocumentation;
    private Class<?> controllerClass;

    public ParsedRoute() {
    }

    public Router.RouteDocumentation getRouteDocumentation() {
        return routeDocumentation;
    }

    public void setRouteDocumentation(final Router.RouteDocumentation routeDocumentation) {
        this.routeDocumentation = routeDocumentation;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(final Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }
}