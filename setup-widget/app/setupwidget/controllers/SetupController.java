package setupwidget.controllers;

import play.Logger;
import setupwidget.models.SphereCredentials;
import play.Configuration;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import setupwidget.views.html.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

@Singleton
public class SetupController extends Controller {
    private static final String CONFIG_SETUP_ENABLED = "application.setup.enabled";
    private static final Form<SphereCredentials> SPHERE_CREDENTIALS_FORM = Form.form(SphereCredentials.class);
    private static final Path PATH = FileSystems.getDefault().getPath("conf", "dev.conf");

    private final boolean setupEnabled;
    private boolean setupComplete;

    @Inject
    public SetupController(final Configuration configuration) {
        this.setupEnabled = configuration.getBoolean(CONFIG_SETUP_ENABLED, true);
        this.setupComplete = doesConfigFileExist();
    }

    public Result handleOrFallback(final Supplier<Result> fallback) {
        final boolean isSetupRequired = setupEnabled && !isSetupComplete();
        return isSetupRequired ? renderForm() : fallback.get();
    }

    public Result renderForm() {
        return onSetupEnabled(() -> ok(setup.render(SPHERE_CREDENTIALS_FORM)));
    }

    public Result processForm() {
        return onSetupEnabled(() -> {
            final Form<SphereCredentials> boundForm = SPHERE_CREDENTIALS_FORM.bindFromRequest();
            final Result result;
            if (boundForm.hasErrors()) {
                result = badRequest(setup.render(boundForm));
            } else {
                final SphereCredentials credentials = boundForm.get();
                final String content = String.format("ctp.projectKey=%s\n" +
                                "ctp.clientId=%s\n" +
                                "ctp.clientSecret=%s\n",
                        credentials.getProjectKey(), credentials.getClientId(), credentials.getClientSecret());
                writeSettingsFile(content);
                result = ok(success.render(PATH.toString()));
            }
            return result;
        });
    }

    private Result onSetupEnabled(final Supplier<Result> action) {
        return setupEnabled ? action.get() : notFound();
    }

    private boolean isSetupComplete() {
        if (!setupComplete) {
            setupComplete = doesConfigFileExist();
            Logger.debug("Setup is complete: " + setupComplete);
        }
        return setupComplete;
    }

    private boolean doesConfigFileExist() {
        return Files.exists(PATH);
    }

    private static void writeSettingsFile(final String content) {
        try {
            Files.write(PATH, content.getBytes());
            Logger.info("CTP credentials saved in " + PATH.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
