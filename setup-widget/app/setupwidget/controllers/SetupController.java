package setupwidget.controllers;

import com.commercetools.sunrise.framework.annotations.ReverseRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import setupwidget.models.SphereCredentials;
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
    private static final Logger logger = LoggerFactory.getLogger(SetupController.class);
    public static final String CONFIG_SETUP_ENABLED = "application.setup.enabled";
    private static final Path PATH =  FileSystems.getDefault().getPath("conf", "dev.conf");

    private boolean setupEnabled = false;
    private boolean setupComplete = false;

    @Inject
    private SetupReverseRouter setupReverseRouter;
    @Inject
    private FormFactory formFactory;

    @Inject
    public void setup(final Configuration configuration) {
        this.setupEnabled = configuration.getBoolean(CONFIG_SETUP_ENABLED, true);
        this.setupComplete = doesConfigFileExist();
    }

    public Result handleOrFallback(final Supplier<Result> fallback) {
        final boolean isSetupRequired = setupEnabled && !isSetupComplete();
        return isSetupRequired ? renderForm() : fallback.get();
    }

    public Result renderForm() {
        return onSetupEnabled(() -> ok(setup.render(formFactory.form(SphereCredentials.class), setupReverseRouter.processSetupFormCall())));
    }

    @ReverseRoute("processSetupFormCall")
    public Result processForm() {
        return onSetupEnabled(() -> {
            final Form<SphereCredentials> form = formFactory.form(SphereCredentials.class).bindFromRequest();
            final Result result;
            if (form.hasErrors()) {
                result = badRequest(setup.render(form, setupReverseRouter.processSetupFormCall()));
            } else {
                final SphereCredentials credentials = form.get();
                final String content = String.format("ctp.projectKey=%s\n" +
                                "ctp.clientId=%s\n" +
                                "ctp.clientSecret=%s\n",
                        credentials.getProjectKey(), credentials.getClientId(), credentials.getClientSecret());
                final Path path = writeSettingsFile(PATH, content);
                result = ok(success.render(path.toString()));
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
            logger.debug("Setup is complete: " + setupComplete);
        }
        return setupComplete;
    }

    private boolean doesConfigFileExist() {
        return Files.exists(PATH);
    }

    private static Path writeSettingsFile(final Path filePath, final String content) {
        try {
            final Path writtenFile = Files.write(filePath, content.getBytes());
            logger.info("CTP credentials saved in " + writtenFile.toString());
            return writtenFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
