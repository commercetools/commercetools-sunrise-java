package inject;

import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.inject.Provider;
import common.templates.HandlebarsTemplateService;
import common.templates.TemplateService;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class TemplateServiceProvider implements Provider<TemplateService> {
    private final Configuration configuration;

    @Inject
    public TemplateServiceProvider(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public TemplateService get() {
        final List<TemplateLoader> templateLoaders = configuration.getConfigList("handlebars.templateLoaders")
                .stream()
                .map(loaderConfig -> {
                    final String type = loaderConfig.getString("type");
                    final String path = loaderConfig.getString("path");
                    if ("classpath".equals(type)) {
                        return new ClassPathTemplateLoader(path);
                    } else if ("file".equals(type)) {
                        return new FileTemplateLoader(path);
                    } else {
                        throw new RuntimeException("cannot build template loader for " + loaderConfig);
                    }
                })
                .collect(toList());
        Logger.debug("Provide HandlebarsTemplateService: "
                + templateLoaders.stream().map(loader -> loader.getPrefix()).collect(joining(", ")));
        return HandlebarsTemplateService.of(templateLoaders);
    }
}
