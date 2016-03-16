package common.templates.handlebars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Base;

import java.io.IOException;

final class HandlebarsJsonHelper<T> extends Base implements Helper<T> {

    @Override
    public CharSequence apply(final T context, final Options options) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(context);
    }
}
