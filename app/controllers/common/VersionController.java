package controllers.common;

import com.commercetools.sunrise.common.version.SunriseVersionController;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import play.Application;

import javax.inject.Inject;

@NoCache
public final class VersionController extends SunriseVersionController {

    @Inject
    public VersionController(final Application application) {
        super(application);
    }
}
