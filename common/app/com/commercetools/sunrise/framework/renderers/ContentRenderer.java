package com.commercetools.sunrise.framework.renderers;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.google.inject.ImplementedBy;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

@ImplementedBy(PageHtmlContentRenderer.class)
public interface ContentRenderer {

    CompletionStage<Content> render(final PageContent pageContent, @Nullable final String templateName, @Nullable final String cmsKey);

    default CompletionStage<Content> render(final PageContent pageContent, @Nullable final String templateName) {
        return render(pageContent, templateName, null);
    }
}
