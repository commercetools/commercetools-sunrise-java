package plugins;

import com.google.inject.*;
import controllers.CountryOperations;
import io.sphere.sdk.client.PlayJavaSphereClient;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import setupwidget.plugins.SetupOnRequestHook;

import java.lang.reflect.Method;

public class Global extends GlobalSettings {

    private Injector injector;
    private final SetupOnRequestHook setupHook = new SetupOnRequestHook();

    @Override
    public void onStart(final Application app) {
        checkDefaultCountry(app);
        super.onStart(app);
        injector = createInjector(app);
    }

    protected Injector createInjector(Application app) {
        return Guice.createInjector(new ProductionModule(app));
    }

    private void checkDefaultCountry(Application app) {
        CountryOperations.of(app.configuration()).defaultCountry();
    }

    @Override
    public void onStop(final Application app) {
        try {
            injector.getInstance(PlayJavaSphereClient.class).close();
        } catch (final ProvisionException e) {
            Logger.debug("Java client not instantiated");
        }
        super.onStop(app);
    }

    @Override
    public <A> A getControllerInstance(final Class<A> controllerClass) throws Exception {
        return injector.getInstance(controllerClass);
    }

    @Override
    public Action onRequest(Http.Request request, Method method) {
        logRequest(request, method);
        return setupHook.onRequest(request, method, () -> super.onRequest(request, method));
    }

    private void logRequest(Http.Request request, Method method) {
        if (Logger.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder(request.toString());
            sb.append(" ").append(method.getDeclaringClass().getCanonicalName());
            sb.append(".").append(method.getName()).append("(");
            Class<?>[] params = method.getParameterTypes();
            for (int j = 0; j < params.length; j++) {
                sb.append(params[j].getCanonicalName().replace("java.lang.", ""));
                if (j < (params.length - 1))
                    sb.append(',');
            }
            sb.append(")");
            Logger.debug(sb.toString());
        }
    }
}
