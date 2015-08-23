package common.pages;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.UserContext;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;

import javax.money.Monetary;
import java.util.Locale;

import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductThumbnailDataFactoryTest {
    @Test
    public void create() {
        final ProductProjection jacket = readObjectFromResource("product.json", ProductProjection.typeReference());

        final UserContext userContext = UserContext.of(CountryCode.DE, Locale.GERMAN, emptyList(), null, Monetary.getCurrency("EUR"), null, null);

        final ProductThumbnailData thumbnailData =
                ProductThumbnailDataFactory.of(userContext).create(jacket);

        assertThat(thumbnailData.getText()).isEqualTo("Freizeitjacke Save the Duck oliv");
        assertThat(thumbnailData.getDescription()).isEqualTo("german test description");
        assertThat(thumbnailData.getImage()).isEqualTo("https://s3-eu-west-1.amazonaws.com/commercetools-maximilian/products/078686_1_large.jpg");
        assertThat(thumbnailData.getPrice()).isEqualTo("EUR 129,00");
    }
}
