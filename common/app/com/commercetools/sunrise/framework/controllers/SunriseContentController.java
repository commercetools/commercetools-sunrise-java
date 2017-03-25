package com.commercetools.sunrise.framework.controllers;

import com.commercetools.sunrise.framework.template.engine.ContentRenderer;

public abstract class SunriseContentController extends SunriseController {

    private final ContentRenderer contentRenderer;

    protected SunriseContentController(final ContentRenderer contentRenderer) {
        this.contentRenderer = contentRenderer;
    }

    public ContentRenderer getContentRenderer() {
        return contentRenderer;
    }
}
