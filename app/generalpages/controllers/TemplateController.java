package generalpages.controllers;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.io.*;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import play.mvc.Result;
import play.twirl.api.Html;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

public class TemplateController extends Controller {
    private static final String DEFAULT_TEMPLATE_PATH = "/templates";
    private static final String OVERRIDE_TEMPLATE_PATH = Play.application().path() + "/app/assets";

    public Result foo() {
        // implement tests to check templates
        return htmlHandlebars("pop").map(html -> ok(html)).orElse(notFound("Template not found"));
    }

    private static Handlebars handlebarsTemplate() {
        final TemplateLoader templateLoader = new ClassPathTemplateLoader(DEFAULT_TEMPLATE_PATH);
        return new Handlebars(templateLoader);
    }

    public static Optional<Html> htmlHandlebars(String templateName) {
        try {
            final Template template = handlebarsTemplate().compile(templateName);
            final Context context = Context
                    .newBuilder(popPageData())
                    .resolver(JavaBeanValueResolver.INSTANCE)
                    .build();
            final String htmlAsString = template.apply(context);
            return Optional.of(new Html(htmlAsString));
        } catch (IOException e) {
            Logger.error("Template " + templateName + " not found", e);
            return Optional.empty();
        }
    }

    private static PopPageData popPageData() {
        return new PopPageData("Sunrise title", breadcrumbs());
    }

    private static List<Breadcrumb> breadcrumbs() {
        return asList(
                new Breadcrumb("home/", "Home", false),
                new Breadcrumb("women/", "Women", false),
                new Breadcrumb("coats/", "Coats", true)
        );
    }
}
