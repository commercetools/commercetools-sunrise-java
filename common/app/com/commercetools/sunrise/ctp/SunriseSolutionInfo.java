package com.commercetools.sunrise.ctp;

import io.sphere.sdk.client.SolutionInfo;

/**
 * Information that is going to be sent to the commercetools platform on each request via the User-Agent header.
 *
 * If you want to provide additional solution info, you can create your own class in your project extending from
 * {@link SolutionInfo} and following the documentation described in that class.
 */
public final class SunriseSolutionInfo extends SolutionInfo {

    public SunriseSolutionInfo(){
        setName("sunrise-java-shop-framework");
        setVersion(BuildInfo.version());
    }
}
