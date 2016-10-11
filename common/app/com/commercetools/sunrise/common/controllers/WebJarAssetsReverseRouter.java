package com.commercetools.sunrise.common.controllers;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionWebJarAssetsReverseRouter.class)
public interface WebJarAssetsReverseRouter {

    Call webJarAssetsCall(final String file);
}
