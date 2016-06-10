/**
 * <p>Sunrise Framework provides overridable controllers, a plugin architecture and default components to speed up the web shop development.</p>
 *
 * <h3>Overridable Controllers</h3>
 *
 * request scoped, implicitly for others
 *
 * <ul>
 *    <li>{@link productcatalog.home.SunriseHomePageController}</li>
 *    <li>{@link productcatalog.productdetail.SunriseProductDetailPageController}</li>
 *    <li>{@link productcatalog.productoverview.SunriseProductOverviewPageController}</li>
 * </ul>
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
