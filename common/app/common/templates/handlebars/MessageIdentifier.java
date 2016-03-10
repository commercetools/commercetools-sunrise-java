package common.templates.handlebars;

import io.sphere.sdk.models.Base;
import org.apache.commons.lang3.StringUtils;

class MessageIdentifier extends Base {
    private final String bundle;
    private final String key;

    public MessageIdentifier(final String context) {
        final String[] parts = StringUtils.split(context, ':');
        final boolean usingDefaultBundle = parts.length == 1;
        this.bundle = usingDefaultBundle ? "main" : parts[0];
        this.key = usingDefaultBundle ? context : parts[1];
    }

    public String getBundle() {
        return bundle;
    }

    public String getKey() {
        return key;
    }
}
