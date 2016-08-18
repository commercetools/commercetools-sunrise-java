package com.commercetools.sunrise.common.controllers;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import com.commercetools.sunrise.common.reverserouter.ReflectionReverseCaller;
import com.commercetools.sunrise.common.reverserouter.ReverseCaller;
import io.sphere.sdk.models.Base;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CompletionException;

@Singleton
final class ReflectionWebJarAssetsReverseRouter extends Base implements WebJarAssetsReverseRouter {

    private final ReverseCaller reverseCaller;

    @Inject
    public ReflectionWebJarAssetsReverseRouter(final ParsedRoutes parsedRoutes) {
        final boolean routeIsPresent = parsedRoutes.getRoutes().stream()
                .filter(p -> "controllers.WebJarAssets.at(file:String)".equals(p.getRouteDocumentation().getControllerMethodInvocation()))
                .findFirst()
                .map(p -> p).isPresent();
        if (routeIsPresent) {
            try {
                final Class<?> routesClass = Class.forName("controllers.routes");
                final Class<?> reverseControllerClass = Class.forName("controllers.ReverseWebJarAssets");
                final Field field = routesClass.getField("WebJarAssets");
                final Object o = field.get(null);
                final Method reverseRouteMethod = reverseControllerClass.getMethod("at", new Class<?>[]{String.class});
                reverseCaller = new ReflectionReverseCaller(reverseRouteMethod, o);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
                throw new CompletionException(e);
            }
        } else {
            throw new CompletionException(new RuntimeException("missing WebJarAssets controller in routes"));
        }
    }

    @Override
    public Call webJarAssetsCall(final String file) {
        return reverseCaller.call(file);
    }
}
