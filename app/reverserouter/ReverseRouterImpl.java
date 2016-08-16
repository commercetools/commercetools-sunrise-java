package reverserouter;

import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.reverserouter.*;
import io.sphere.sdk.models.Base;
import play.mvc.Call;
import setupwidget.controllers.SetupReverseRouter;

import static demo.myaccount.routes.*;
import static demo.productcatalog.routes.*;
import static demo.shoppingcart.routes.*;
import static setupwidget.controllers.routes.*;

public class ReverseRouterImpl extends Base implements ReverseRouter, HomeReverseRouter, SetupReverseRouter {

    @Override
    public Call themeAssets(final String file) {
        return controllers.routes.WebJarAssets.at(file);
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return HomeController.show(languageTag);
    }

    @Override
    public Call processSetupFormCall() {
        return SetupController.processForm();
    }
}
