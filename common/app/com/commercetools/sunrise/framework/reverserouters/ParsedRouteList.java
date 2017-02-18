package com.commercetools.sunrise.framework.reverserouters;

import io.sphere.sdk.models.Base;
import play.routing.Router;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public final class ParsedRouteList extends Base {

    private final List<ParsedRoute> routes;

    @Inject
    ParsedRouteList(final Router router) {
        this.routes = router.documentation().stream()
                .map(ParsedRoute::of)
                .collect(Collectors.toList());
    }

    public List<ParsedRoute> getRoutes() {
        return routes;
    }
}