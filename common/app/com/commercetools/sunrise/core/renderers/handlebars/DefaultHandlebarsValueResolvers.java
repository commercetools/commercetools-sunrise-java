package com.commercetools.sunrise.core.renderers.handlebars;

import com.github.jknack.handlebars.ValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Singleton
public class DefaultHandlebarsValueResolvers implements HandlebarsValueResolvers {

    private final PlayJavaFormResolver playJavaFormResolver;
    private final SunriseJavaBeanValueResolver sunriseJavaBeanValueResolver;

    @Inject
    protected DefaultHandlebarsValueResolvers(final PlayJavaFormResolver playJavaFormResolver,
                                              final SunriseJavaBeanValueResolver sunriseJavaBeanValueResolver) {
        this.playJavaFormResolver = playJavaFormResolver;
        this.sunriseJavaBeanValueResolver = sunriseJavaBeanValueResolver;
    }

    @Override
    public List<ValueResolver> valueResolvers() {
        final List<ValueResolver> valueResolvers = new ArrayList<>();
        valueResolvers.addAll(asList(MapValueResolver.INSTANCE, playJavaFormResolver, sunriseJavaBeanValueResolver));
        return valueResolvers;
    }
}
