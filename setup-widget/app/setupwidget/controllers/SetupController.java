package setupwidget.controllers;

import play.Logger;
import setupwidget.models.SphereCredentials;
import play.Configuration;
import play.data.Form;
import play.libs.F;
import play.mvc.Controller;
import play.mvc.Result;
import setupwidget.views.html.setup;
import setupwidget.views.html.success;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import static play.libs.F.Promise.pure;

@Singleton
public class SetupController extends Controller {
    private static final Form<SphereCredentials> SPHERE_CREDENTIALS_FORM = Form.form(SphereCredentials.class);
    private static final Path PATH = FileSystems.getDefault().getPath("conf", "dev.conf");

    private final Configuration config;
    private boolean setupComplete;

    @Inject
    public SetupController(final Configuration config) {
        this.config = config;
        this.setupComplete = doesConfigFileExist();
    }

    public Result handleOrFallback(final Supplier<Result> fallback) {
        final boolean isWidgetActionsRequired = isSettingsWidgetEnabled() && !isSetupComplete();
        return isWidgetActionsRequired ? renderForm() : fallback.get();
    }

    public Result renderForm() {
        return onWidgetEnabled(() -> ok(setup.render(SPHERE_CREDENTIALS_FORM)));
    }

    public Result processForm() {
        return onWidgetEnabled(() -> {
            final Form<SphereCredentials> boundForm = SPHERE_CREDENTIALS_FORM.bindFromRequest();
            final Result result;
            if (boundForm.hasErrors()) {
                result = badRequest(setup.render(boundForm));
            } else {
                final SphereCredentials credentials = boundForm.get();
                final String content = String.format("sphere.project=%s\n" +
                                "sphere.clientId=%s\n" +
                                "sphere.clientSecret=%s\n",
                        credentials.getProjectKey(), credentials.getClientId(), credentials.getClientSecret());
                writeSettingsFile(content);
                result = ok(success.render(PATH.toString()));
            }
            return result;
        });
    }

    private Result onWidgetEnabled(final Supplier<Result> action) {
        return isSettingsWidgetEnabled() ? action.get() : notFound();
    }

    private static void writeSettingsFile(String content) {
        try {
            Files.write(PATH, content.getBytes());
            Logger.info("SPHERE.IO credentials saved in " + PATH.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isSettingsWidgetEnabled() {
        return config.getBoolean("application.settingsWidget.enabled", true);
    }

    private boolean isSetupComplete() {
        if (!setupComplete) {
            setupComplete = doesConfigFileExist();
            Logger.debug("Setup is completed: " + setupComplete);
        }
        return setupComplete;
    }

    private boolean doesConfigFileExist() {
        return Files.exists(PATH);
    }
}
