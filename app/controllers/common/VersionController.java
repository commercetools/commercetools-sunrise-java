package controllers.common;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.common.SunriseVersionController;
import play.Application;

import javax.inject.Inject;

@NoCache
public final class VersionController extends SunriseVersionController {

    @Inject
    public VersionController(final Application application) {
        super(application);
    }
}
