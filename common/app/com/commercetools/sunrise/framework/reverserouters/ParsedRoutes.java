package com.commercetools.sunrise.framework.reverserouters;

import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;
import play.routing.Router;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;

import static com.commercetools.sunrise.framework.reverserouters.ReflectionUtils.getClassByName;
import static org.apache.commons.lang3.StringUtils.countMatches;

@Singleton
public final class ParsedRoutes extends Base {

    private final List<ParsedRoute> routes;

    @Inject
    ParsedRoutes(final Router router) {
        this.routes = router.documentation().stream()
                .map(ParsedRoutes::parseRoute)
                .collect(Collectors.toList());
    }

    public List<ParsedRoute> getRoutes() {
        return routes;
    }

    private static ParsedRoute parseRoute(final Router.RouteDocumentation routeDocumentation) {
        final String controllerMethodInvocation = routeDocumentation.getControllerMethodInvocation();
        if (countMatches(controllerMethodInvocation, '@') == 2) {
            final String controllerMethod = StringUtils.removeStart(controllerMethodInvocation, "@");
            final String controllerClassName = controllerMethod.substring(0, controllerMethod.indexOf("@"));
            try {
                return ParsedRoute.of(routeDocumentation, getClassByName(controllerClassName));
            } catch (ClassNotFoundException e) {
                throw new CompletionException(e);
            }
        }
        return ParsedRoute.of(routeDocumentation, null);
    }
}