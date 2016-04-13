package common.template.engine.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.HighConcurrencyTemplateCache;
import com.github.jknack.handlebars.io.TemplateLoader;
import common.template.cms.CmsPage;
import common.controllers.PageData;
import common.template.i18n.I18nResolver;
import common.template.engine.TemplateNotFoundException;
import common.template.engine.TemplateRenderException;
import common.template.engine.TemplateService;
import play.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

public final class HandlebarsTemplateService implements TemplateService {
    static final String LANGUAGE_TAGS_IN_CONTEXT_KEY = "app-language-tags";
    static final String CMS_PAGE_IN_CONTEXT_KEY = "app-cms-page";
    private final Handlebars handlebars;

    private HandlebarsTemplateService(final Handlebars handlebars) {
        this.handlebars = handlebars;
    }

    @Override
    public String render(final String templateName, final PageData pageData, final List<Locale> locales, final CmsPage cmsPage) {
        final Template template = compileTemplate(templateName);
        final Context context = createContext(pageData, locales, cmsPage);
        try {
            Logger.debug("Rendering template " + templateName);
            return template.apply(context);
        } catch (IOException e) {
            throw new TemplateRenderException("Context could not be applied to template " + templateName, e);
        }
    }

    public static TemplateService of(final List<TemplateLoader> templateLoaders, final I18nResolver i18NResolver) {
        final TemplateLoader[] loaders = templateLoaders.toArray(new TemplateLoader[templateLoaders.size()]);
        final Handlebars handlebars = new Handlebars()
                .with(loaders)
                .with(new HighConcurrencyTemplateCache())
                .infiniteLoops(true);
        handlebars.registerHelper("i18n", new HandlebarsI18nHelper(i18NResolver));
        handlebars.registerHelper("cms", new HandlebarsCmsHelper());
        handlebars.registerHelper("json", new HandlebarsJsonHelper<>());
        return new HandlebarsTemplateService(handlebars);
    }

    private Context createContext(final PageData pageData, final List<Locale> locales, final CmsPage cmsPage) {
        final Context context = Context.newContext(pageData);
        context.data(LANGUAGE_TAGS_IN_CONTEXT_KEY, locales.stream().map(Locale::toLanguageTag).collect(toList()));
        context.data(CMS_PAGE_IN_CONTEXT_KEY, cmsPage);
        return context;
    }

    private Template compileTemplate(final String templateName) {
        try {
            return handlebars.compile(templateName);
        } catch (IOException e) {
            throw new TemplateNotFoundException("Could not find the default template", e);
        }
    }
}
