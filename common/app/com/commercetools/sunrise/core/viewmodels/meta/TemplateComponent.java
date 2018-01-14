package com.commercetools.sunrise.core.viewmodels.meta;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.filters.csrf.CSRF;
import play.mvc.Http;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class TemplateComponent implements ControllerComponent, PageDataHook {

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return completedFuture(pageData
                .put("csrfToken", csrfToken().orElse(null)));
    }

    private Optional<String> csrfToken() {
        return CSRF.getToken(Http.Context.current().request())
                .map(CSRF.Token::value);
    }
}
