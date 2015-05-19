package generalpages.controllers;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import models.PageData;
import models.PopPageData;
import play.Logger;
import play.twirl.api.Html;

import java.io.IOException;
import java.util.Optional;

public class HandlebarsViewService implements ViewService {
    private final TemplateService templateService;

    HandlebarsViewService(final TemplateService templateService) {
        this.templateService = templateService;
    }

    @Override
    public Html popPage(final PopPageData data) {
//        return views.html.pop(data);
        return null;
    }

    private Optional<Html> html(final String templateName, final PageData pageData) {
        try {
            final Template template = templateService.compile(templateName);
            final Context context = Context
                    .newBuilder(pageData)
                    .resolver(JavaBeanValueResolver.INSTANCE)
                    .build();
            final String htmlAsString = template.apply(context);
            final Html html = new Html(htmlAsString);
            return Optional.of(html);
        } catch (IOException e) {
            Logger.error("Template " + templateName + " could not be filled properly", e);
            return Optional.empty();
        }
    }
}
