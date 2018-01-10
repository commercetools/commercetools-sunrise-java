package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.viewmodels.OldPageData;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public interface WithContent {

    default CompletionStage<Content> renderContent(final PageData pageData) {
        return getContentRenderer().render(pageData, getTemplateName(), getCmsPageKey());
    }

    default CompletionStage<Result> okResultWithPageContent(final PageData pageData) {
        return renderContent(pageData)
                .thenApplyAsync(Results::ok, HttpExecution.defaultContext());
    }

    default CompletionStage<Result> badRequestResultWithPageContent(final PageData pageData) {
        return renderContent(pageData)
                .thenApplyAsync(Results::badRequest, HttpExecution.defaultContext());
    }

    default CompletionStage<Result> internalServerErrorResultWithPageContent(final PageData pageData) {
        return renderContent(pageData)
                .thenApplyAsync(Results::internalServerError, HttpExecution.defaultContext());
    }

    ContentRenderer getContentRenderer();

    @Nullable
    default String getTemplateName() {
        return null;
    }

    @Nullable
    default String getCmsPageKey() {
        return null;
    }
}
