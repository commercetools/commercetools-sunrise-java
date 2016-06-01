package wedecidelatercommon;

import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import play.mvc.Call;

import java.util.Locale;
import java.util.Optional;

public interface ProductReverseRouter {

    default String showProductUrlOrEmpty(final Locale locale, final ProductProjection product, final ProductVariant productVariant) {
        return showProduct(locale, product, productVariant).map(Call::url).orElse("");
    }

    default String showProductUrlOrEmpty(final Locale locale, final LineItem lineItem) {
        return showProduct(locale, lineItem).map(Call::url).orElse("");
    }

    Optional<Call> showProduct(final Locale locale, final ProductProjection product, final ProductVariant productVariant);

    Optional<Call> showProduct(final Locale locale, final LineItem lineItem);
}
