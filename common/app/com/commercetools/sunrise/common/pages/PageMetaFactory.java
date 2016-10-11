package com.commercetools.sunrise.common.pages;

import com.google.inject.ImplementedBy;

@ImplementedBy(PageMetaFactoryImpl.class)
public interface PageMetaFactory {

    PageMeta create();
}
