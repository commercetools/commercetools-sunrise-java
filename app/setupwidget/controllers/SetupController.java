package setupwidget.controllers;

import play.Play;
import setupwidget.models.SphereCredentials;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import setupwidget.plugins.SetupOnRequestHook;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;

public class SetupController extends Controller {

    private static final Form<SphereCredentials> sphereCredentialsForm = Form.form(SphereCredentials.class);

    public static Result renderForm() {
        return onWidgetEnabled(SetupController::renderFormImpl);
    }

    public static Result processForm() {
        return onWidgetEnabled(SetupController::processFormImpl);
    }

    private static Result renderFormImpl() {
        return ok(setupwidget.views.html.setup.render(sphereCredentialsForm));
    }

    private static Result processFormImpl() {
        final Form<SphereCredentials> filledForm = sphereCredentialsForm.bindFromRequest();
        final Result result;
        if (filledForm.hasErrors()) {
            result = badRequest(setupwidget.views.html.setup.render(filledForm));
        } else {
            final SphereCredentials credentials = filledForm.get();
            final String content = String.format("sphere.project=%s\n" +
                            "sphere.clientId=%s\n" +
                            "sphere.clientSecret=%s\n",
                    credentials.getProjectKey(), credentials.getClientId(), credentials.getClientSecret());
            writeSettingsFile(content);
            result = ok(setupwidget.views.html.success.render());
        }
        return result;
    }

    private static void writeSettingsFile(String content) {
        try {
            Files.write(Paths.get("conf/dev.conf"), content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Result onWidgetEnabled(Supplier<Result> action) {
        if (SetupOnRequestHook.isEnabled()) {
            return action.get();
        } else {
            return notFound();
        }
    }
}
