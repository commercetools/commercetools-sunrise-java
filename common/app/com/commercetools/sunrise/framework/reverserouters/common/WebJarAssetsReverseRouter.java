package com.commercetools.sunrise.framework.reverserouters.common;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionWebJarAssetsReverseRouter.class)
public interface WebJarAssetsReverseRouter {

    Call webJarAssetsCall(final String filepath);
}
