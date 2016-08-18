import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;
import com.commercetools.sunrise.productcatalog.productdetail.SunriseProductDetailController;
import com.commercetools.sunrise.productcatalog.productoverview.SunriseProductOverviewController;
import com.commercetools.sunrise.productcatalog.productsuggestions.ProductSuggestionsControllerComponent;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.hooks.Hook;
import com.commercetools.sunrise.hooks.events.RequestStartedHook;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.common.contexts.RequestScope;

/**
 * <p>Sunrise Framework provides overridable controllers, a plugin architecture and default components to speed up the web shop development.</p>
 *
 * <h3>Overridable Controllers</h3>
 *
 * request scoped, implicitly for others
 *
 * <ul>
 *    <li>{@link SunriseHomeController}</li>
 *    <li>{@link SunriseProductDetailController}</li>
 *    <li>{@link SunriseProductOverviewController}</li>
 * </ul>
 *
 * <h3>Controller Components</h3>
 * {@link com.commercetools.sunrise.framework.ControllerComponent}s are plugins for controllers which communicate with the controller on the one hand
 * via dependency-injected fields like {@link UserContext} and on the other hand via {@link Hook}s of the controller.
 * A controller component instance lives in the {@link RequestScope} so they assist with one HTTP request and then for the next HTTP request another instance is created.
 * Controller components implement the hooks of the controllers where they need to do sth. for example implementing {@link PageDataReadyHook}
 * enables to change the content that will be rendered, the hook is called when all asynchronous requests are completed. The hook {@link RequestStartedHook} enables to do sth. when a request comes in like log stuff, call external systems.
 * The hook {@link ProductProjectionSearchHook} enables to modify a search request for example to add extension paths.
 * For a good explanation how such a component works look into the <strong>source code</strong> of {@link ProductSuggestionsControllerComponent}.
 *
 * <!-- multi controller components -->
 *
 * <h3>Tags</h3>
 * <p>Tags can be used as facility to check if a multi-controller-component should be used for a controller.</p>
 * <p>The tags for a controller are documented in the Sunrise Framework controller class.</p>
 * <p>Normally a controller contains a tag for the module, like "product-catalog", which it belongs to.</p>
 * <p>Tags should match the regular expression {@code [a-z][-a-z0-9]+}, so they don't contain uppercase letters and words are separated by a dash. They do not contain "page" or "controller" and they should not be accronyms like "pdp" for "product-detail-page".</p>
 *
 *
 */
public class Documentation {
}
