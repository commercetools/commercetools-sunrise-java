package plugins;

import controllers.CountryOperations;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http;
import setupwidget.plugins.SetupOnRequestHook;

import java.lang.reflect.Method;

public class Global extends GlobalSettings {

    private final SetupOnRequestHook setupHook = new SetupOnRequestHook();

    @Override
    public void onStart(final Application app) {
        checkDefaultCountry(app);
        super.onStart(app);
    }

    private void checkDefaultCountry(final Application app) {
        CountryOperations.of(app.configuration()).defaultCountry();
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
