package common.templates.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import common.cms.CmsService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

final class HandlebarsCmsHelper extends HandlebarsHelperBase implements Helper<String> {
    private static final int ALLOWED_TIMEOUT = 5000;
    private final CmsService cmsService;

    public HandlebarsCmsHelper(final CmsService cmsService) {
        this.cmsService = cmsService;
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<Locale> locales = getLocalesFromContext(options);
        final MessageIdentifier messageIdentifier = new MessageIdentifier(context);
        return cmsService.getPage(locales, messageIdentifier.getBundle()) // TODO avoid doing this for every cms occurrence
                .map(page -> page.getOrEmpty(messageIdentifier.getKey(), options.hash))
                .get(ALLOWED_TIMEOUT, TimeUnit.MILLISECONDS);
    }
}
