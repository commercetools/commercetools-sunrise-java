package common.templates.handlebars;

import io.sphere.sdk.models.Base;

import static common.templates.handlebars.HelperUtils.getPart;
import static org.apache.commons.lang3.StringUtils.split;

class I18nIdentifier extends Base {
    private static final String DEFAULT_BUNDLE = "main";
    private final String bundle;
    private final String key;

    public I18nIdentifier(final String context) {
        final String[] parts = split(context, ":", 2);
        this.key = getPart(parts, 1, context);
        if (context.equals(key)) {
            this.bundle = DEFAULT_BUNDLE;
        } else {
            this.bundle = getPart(parts, 0, DEFAULT_BUNDLE);
        }
    }

    public String getBundle() {
        return bundle;
    }

    public String getKey() {
        return key;
    }
}
