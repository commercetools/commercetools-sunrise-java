package com.commercetools.sunrise.ctp;

import io.sphere.sdk.client.SolutionInfo;

public class SunriseSolutionInfo extends SolutionInfo {

    public SunriseSolutionInfo(){
        setName("sunrise-java-shop-framework");
        setVersion(BuildInfo.version());
    }
}
