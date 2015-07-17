package common.utils;

import common.contexts.AppContext;
import io.sphere.sdk.models.LocalizedStrings;

public class Languages {

    public static String translate(final LocalizedStrings localizedStrings, final AppContext context) {
        return localizedStrings.get(context.user().language())
                .orElse(localizedStrings.get(context.user().fallbackLanguages())
                        .orElse(localizedStrings.get(context.project().languages()).orElse("")));
    }
}
