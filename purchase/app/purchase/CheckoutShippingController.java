package purchase;

import common.controllers.ControllerDependency;
import play.libs.F;
import play.mvc.Result;

import javax.inject.Inject;

public class CheckoutShippingController extends CartController {

    @Inject
    public CheckoutShippingController(final ControllerDependency controllerDependency, final ShippingMethods shippingMethods) {
        super(controllerDependency);
    }

    public F.Promise<Result> show(final String languageTag) {
        return F.Promise.pure(TODO);
    }

    public F.Promise<Result> process(final String languageTag) {
        return F.Promise.pure(TODO);
    }

}