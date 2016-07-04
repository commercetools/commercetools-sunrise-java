package com.commercetools.sunrise.hooks;

import com.commercetools.sunrise.common.pages.PageData;

public interface PageDataHook extends Hook {
    void acceptPageData(final PageData pageData);
}
