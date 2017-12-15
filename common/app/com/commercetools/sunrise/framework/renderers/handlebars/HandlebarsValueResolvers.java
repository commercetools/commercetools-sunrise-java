package com.commercetools.sunrise.framework.renderers.handlebars;

import com.github.jknack.handlebars.ValueResolver;
import com.google.inject.ImplementedBy;

import java.util.List;

@ImplementedBy(DefaultHandlebarsValueResolvers.class)
public interface HandlebarsValueResolvers {

    List<ValueResolver> valueResolvers();
}
