package com.commercetools.sunrise.framework.template.engine.handlebars;

import com.commercetools.sunrise.framework.SunriseModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;

import java.io.IOException;

final class HandlebarsJsonHelper<T> extends SunriseModel implements Helper<T> {

    @Override
    public CharSequence apply(final T context, final Options options) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(context);
    }
}
