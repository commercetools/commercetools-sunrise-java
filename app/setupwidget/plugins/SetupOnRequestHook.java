package setupwidget.plugins;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import setupwidget.controllers.SetupController;
import setupwidget.controllers.routes;

import java.lang.reflect.Method;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class SetupOnRequestHook {
    public static final Path PATH = FileSystems.getDefault().getPath("conf", "dev.conf");

    private boolean setupComplete = configFileExists();

    private boolean configFileExists() {
        return Files.exists(PATH);
    }

    public Action onRequest(final Http.Request request, final Method method, final Supplier<Action> fallback) {
        final Action result;
        if (play.Play.isDev()) {
            result = provideWidgetToSetCredentialsToAConfFile(request, fallback);
        } else {
            result = fallback.get();
        }
        return result;
    }

    private Action provideWidgetToSetCredentialsToAConfFile(Http.Request request, Supplier<Action> fallback) {
        Action result;
        if (isSetupComplete() || isCredentialsSubmitPage(request)) {
            result = fallback.get();
        } else {
            result = new Action.Simple() {
                public F.Promise<Result> call(Http.Context ctx) throws Throwable {
                    return F.Promise.pure(SetupController.renderForm());
                }
            };
        }
        return result;
    }

    private boolean isSetupComplete() {
        if (!setupComplete) {
            setupComplete = configFileExists();
        }
        return setupComplete;
    }

    private boolean isCredentialsSubmitPage(Http.Request request) {
        return routes.SetupController.processForm().url().equals(request.path());
    }


}
