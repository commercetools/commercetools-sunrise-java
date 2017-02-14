package demo.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.controllers.SunriseVersionController;
import play.Application;

import javax.inject.Inject;

@NoCache
public final class VersionController extends SunriseVersionController {

    @Inject
    public VersionController(final Application application) {
        super(application);
    }
}
