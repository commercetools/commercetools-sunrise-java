package demo.myaccount;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.SunriseAddressBookController;
import com.commercetools.sunrise.myaccount.addressbook.addresslist.view.AddressBookPageContentFactory;
import demo.CommonControllerComponentListSupplier;
import demo.PageHeaderControllerComponentListSupplier;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class AddressBookController extends SunriseAddressBookController {

    private final AuthenticationReverseRouter authenticationReverseRouter;

    @Inject
    public AddressBookController(final ComponentRegistry componentRegistry,
                                 final TemplateRenderer templateRenderer,
                                 final CustomerFinder customerFinder,
                                 final AddressBookPageContentFactory addressBookPageContentFactory,
                                 final AuthenticationReverseRouter authenticationReverseRouter) {
        super(componentRegistry, templateRenderer, customerFinder, addressBookPageContentFactory);
        this.authenticationReverseRouter = authenticationReverseRouter;
    }

    @Inject
    public void registerComponents(final CommonControllerComponentListSupplier commonControllerComponentListSupplier,
                                   final PageHeaderControllerComponentListSupplier pageHeaderControllerComponentListSupplier) {
        register(commonControllerComponentListSupplier);
        register(pageHeaderControllerComponentListSupplier);
    }

    @Override
    public CompletionStage<Result> handleNotFoundCustomer() {
        return redirectTo(authenticationReverseRouter.showLogInForm());
    }
}
