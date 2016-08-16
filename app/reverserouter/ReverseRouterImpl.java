package reverserouter;

import com.commercetools.sunrise.common.controllers.ReverseRouter;
import io.sphere.sdk.models.Base;
import play.mvc.Call;

public class ReverseRouterImpl extends Base implements ReverseRouter {

    @Override
    public Call themeAssets(final String file) {
        return controllers.routes.WebJarAssets.at(file);
    }
}
