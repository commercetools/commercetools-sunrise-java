package com.commercetools.sunrise.common.pages;

import com.google.inject.ImplementedBy;

@ImplementedBy(PageNavMenuFactoryImpl.class)
public interface PageNavMenuFactory {
    PageNavMenu create();
}
