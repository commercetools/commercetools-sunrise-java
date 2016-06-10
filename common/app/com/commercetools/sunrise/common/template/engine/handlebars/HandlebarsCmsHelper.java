package com.commercetools.sunrise.common.template.engine.handlebars;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.commercetools.sunrise.common.template.cms.CmsIdentifier;
import com.commercetools.sunrise.common.template.cms.CmsService;
import io.sphere.sdk.models.Base;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

final class HandlebarsCmsHelper extends Base implements Helper<String> {

    private final CmsService cmsService;

    public HandlebarsCmsHelper(final CmsService cmsService) {
        this.cmsService = cmsService;
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final CmsIdentifier cmsIdentifier = CmsIdentifier.of(context);
        final List<Locale> locales = HelperUtils.getLocalesFromContext(options.context);
        return cmsService.getOrEmpty(locales, cmsIdentifier).toCompletableFuture().join();
    }
}
