package wedecidelatercommon;

import com.commercetools.sunrise.common.controllers.SunrisePageData;
import com.commercetools.sunrise.common.hooks.SunrisePageDataHook;
import framework.ControllerComponent;
import play.mvc.Http;
import shoppingcart.CartSessionUtils;

import javax.inject.Inject;

public class MiniCartControllerComponent implements ControllerComponent, SunrisePageDataHook {
    @Inject
    private Http.Context context;

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        sunrisePageData.getHeader()
                .setMiniCart(CartSessionUtils.getMiniCart(context.session()));
    }
}
