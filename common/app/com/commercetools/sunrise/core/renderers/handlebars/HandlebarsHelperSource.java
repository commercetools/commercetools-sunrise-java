package com.commercetools.sunrise.core.renderers.handlebars;

import com.google.inject.ImplementedBy;

/**
 * Source of helpers to be registered to Handlebars.
 *
 * @see com.github.jknack.handlebars.Handlebars#registerHelpers(Object)
 * @see com.github.jknack.handlebars.Handlebars#registerHelpers(Class)
 */
@ImplementedBy(DefaultHandlebarsHelperSource.class)
public interface HandlebarsHelperSource {

}
