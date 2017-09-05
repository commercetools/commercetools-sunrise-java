package com.commercetools.sunrise.framework.template.engine;

import com.commercetools.sunrise.cms.CmsService;
import com.commercetools.sunrise.framework.localization.UserLanguage;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class EmailHtmlContentRenderer extends AbstractHtmlContentRenderer implements EmailContentRenderer {

    @Inject
    EmailHtmlContentRenderer(final UserLanguage userLanguage, final TemplateEngine templateEngine, final CmsService cmsService) {
        super(userLanguage, templateEngine, cmsService);
    }

    @Override
    public CompletionStage<Content> render(final PageContent pageContent, @Nullable final String templateName, @Nullable final String cmsKey) {
        final PageData pageData = new PageData();
        pageData.setContent(pageContent);
        return render(pageData, templateName, cmsKey);
    }
}
