package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;
import play.mvc.Results;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public interface WithContent {

    TemplateEngine getTemplateEngine();

    default CompletionStage<Result> render(final int status, final PageData pageData) {
        return getTemplateEngine().render(getTemplateName(), pageData)
                .thenApply(content -> Results.status(status, content));
    }

    @Nullable
    default String getTemplateName() {
        return null;
    }
}
