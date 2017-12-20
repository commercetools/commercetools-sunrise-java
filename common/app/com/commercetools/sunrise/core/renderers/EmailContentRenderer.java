package com.commercetools.sunrise.core.renderers;

import com.commercetools.sunrise.core.viewmodels.PageData;
import com.google.inject.ImplementedBy;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

@ImplementedBy(EmailHtmlContentRenderer.class)
public interface EmailContentRenderer extends ContentRenderer {

    @Override
    CompletionStage<Content> render(final PageData pageData, @Nullable final String templateName, @Nullable final String cmsKey);
}
