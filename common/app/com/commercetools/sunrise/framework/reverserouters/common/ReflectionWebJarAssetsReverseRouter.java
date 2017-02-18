package com.commercetools.sunrise.framework.reverserouters.common;

import com.commercetools.sunrise.framework.reverserouters.ReflectionReverseCaller;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import com.commercetools.sunrise.framework.reverserouters.ParsedRouteList;
import io.sphere.sdk.models.Base;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CompletionException;

import static com.commercetools.sunrise.common.utils.ReflectionUtils.getClassByName;

@Singleton
final class ReflectionWebJarAssetsReverseRouter extends Base implements WebJarAssetsReverseRouter {

    private final ReverseCaller reverseCaller;

    @Inject
    ReflectionWebJarAssetsReverseRouter(final ParsedRouteList parsedRouteList) {
        if (isWebJarsRoutePresent(parsedRouteList)) {
            try {
                final Method reverseRouteMethod = getMethod();
                final Object object = getField().get(null);
                this.reverseCaller = ReflectionReverseCaller.of(reverseRouteMethod, object);
            } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | NoSuchMethodException e) {
                throw new CompletionException(e);
            }
        } else {
            throw new CompletionException(new RuntimeException("missing WebJarAssets controller in routes"));
        }
    }

    private static boolean isWebJarsRoutePresent(final ParsedRouteList parsedRouteList) {
        return parsedRouteList.getRoutes().stream()
                .map(route -> route.getRouteDocumentation().getControllerMethodInvocation())
                .map(methodInvocation -> methodInvocation.replace("@", ""))
                .anyMatch("controllers.WebJarAssets.at(file:String)"::equals);
    }

    private static Method getMethod() throws ClassNotFoundException, NoSuchMethodException {
        final Class<?> reverseControllerClass = getClassByName("controllers.ReverseWebJarAssets");
        return reverseControllerClass.getMethod("at", String.class);
    }

    private static Field getField() throws ClassNotFoundException, NoSuchFieldException {
        final Class<?> routesClass = getClassByName("controllers.routes");
        return routesClass.getField("WebJarAssets");
    }

    @Override
    public Call webJarAssetsCall(final String file) {
        return reverseCaller.call(file);
    }
}
