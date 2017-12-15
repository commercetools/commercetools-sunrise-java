package com.commercetools.sunrise.core.renderers.handlebars;

import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.inject.ImplementedBy;
import play.Configuration;

import java.util.List;

@ImplementedBy(HandlebarsSettingsImpl.class)
public interface HandlebarsSettings {

    /**
     * @return list of template loaders in the order they are going to be invoked
     */
    List<TemplateLoader> templateLoaders();

    static HandlebarsSettings of(final Configuration globalConfig, final String configPath) {
        return new HandlebarsSettingsImpl(globalConfig, configPath);
    }
}