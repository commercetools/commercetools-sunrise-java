package setupwidget.controllers;

import play.Configuration;
import play.libs.F;
import setupwidget.models.SphereCredentials;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

@Singleton
public class SetupController extends Controller {

    private static final Form<SphereCredentials> sphereCredentialsForm = Form.form(SphereCredentials.class);
    private final Configuration config;
    public static final Path PATH = FileSystems.getDefault().getPath("conf", "dev.conf");

    private boolean setupComplete = configFileExists();

    private boolean configFileExists() {
        return Files.exists(PATH);
    }

    @Inject
    public SetupController(final Configuration config) {
        this.config = config;
    }

    public Result renderForm() {
        return onWidgetEnabled(SetupController::renderFormImpl);
    }

    public Result processForm() {
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

    private Result onWidgetEnabled(final Supplier<Result> action) {
        if (settingsWidgetIsEnabled()) {
            return action.get();
        } else {
            return notFound();
        }
    }

    private boolean settingsWidgetIsEnabled() {
        return config.getBoolean("application.settingsWidget.enabled", true);
    }

    public F.Promise<Result> handleOrFallback(final Supplier<F.Promise<Result>> fallback) {
        final boolean widgetActionsRequired = settingsWidgetIsEnabled() && (!isSetupComplete());

        System.err.println("booleans");
        System.err.println(settingsWidgetIsEnabled());
        System.err.println(isSetupComplete());
        System.err.println(widgetActionsRequired);

        return widgetActionsRequired ? handle() : fallback.get();
    }

    private boolean isSetupComplete() {
        if (!setupComplete) {
            setupComplete = configFileExists();
        }
        return setupComplete;
    }

    private F.Promise<Result> handle() {
        return F.Promise.pure(renderForm());
    }
}
