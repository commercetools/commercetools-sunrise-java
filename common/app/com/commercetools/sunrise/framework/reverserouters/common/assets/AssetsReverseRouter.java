package com.commercetools.sunrise.framework.reverserouters.common.assets;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultAssetsReverseRouter.class)
public interface AssetsReverseRouter {

    String ASSET = "assetCall";

    Call assetCall(final String filepath);
}
