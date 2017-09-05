package com.commercetools.sunrise.framework.template.engine;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.google.inject.ImplementedBy;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

@ImplementedBy(EmailHtmlContentRenderer.class)
public interface EmailContentRenderer extends ContentRenderer {

    @Override
    CompletionStage<Content> render(final PageContent pageContent, @Nullable final String templateName, @Nullable final String cmsKey);
}
