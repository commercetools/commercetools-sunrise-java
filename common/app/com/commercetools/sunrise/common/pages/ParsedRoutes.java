package com.commercetools.sunrise.common.pages;

import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;
import play.routing.Router;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import static com.commercetools.sunrise.common.reverserouter.ReflectionUtils.getClassByName;
import static org.apache.commons.lang3.StringUtils.countMatches;

@Singleton
public final class ParsedRoutes extends Base {
    private List<ParsedRoute> routes;

    @Inject
    public void setRouter(final Router router) {
        routes = parse(router);
    }

    private List<ParsedRoute> parse(final Router router) {
        return router.documentation().stream()
                .map(this::parse)
                .collect(Collectors.toList());
    }

    private ParsedRoute parse(final Router.RouteDocumentation rd) {
        final ParsedRoute parsedRoute = new ParsedRoute();
        parsedRoute.setRouteDocumentation(rd);
        if (countMatches(rd.getControllerMethodInvocation(), '@') == 2) {
            final String s = StringUtils.removeStart(rd.getControllerMethodInvocation(), "@");
            final String controllerClassName = s.substring(0, s.indexOf("@"));
            try {
                final Class<?> clazz = getClassByName(controllerClassName);
                parsedRoute.setControllerClass(clazz);
            } catch (ClassNotFoundException e) {
                throw new CompletionException(e);
            }
        }
        return parsedRoute;
    }

    public List<ParsedRoute> getRoutes() {
        return routes;
    }
}