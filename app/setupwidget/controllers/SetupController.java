package setupwidget.controllers;

import setupwidget.models.SphereCredentials;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SetupController extends Controller {

    private static final Form<SphereCredentials> sphereCredentialsForm = Form.form(SphereCredentials.class);

    public static Result renderForm() {
        return ok(setupwidget.views.html.setup.render(sphereCredentialsForm));
    }

    //TODO disable in prod
    public static Result processForm() throws IOException {
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
            Files.write(Paths.get("conf/dev.conf"), content.getBytes());
            result = ok(setupwidget.views.html.success.render());
        }
        return result;
    }
}
