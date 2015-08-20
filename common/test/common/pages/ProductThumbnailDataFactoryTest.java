package common.pages;

import com.neovisionaries.i18n.CountryCode;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.products.ProductProjection;
import org.junit.Test;

import javax.money.Monetary;
import java.util.Locale;
import java.util.Optional;

import static io.sphere.sdk.json.JsonUtils.readObjectFromResource;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductThumbnailDataFactoryTest {
    private final Translator translator = Translator.of(Locale.GERMAN, emptyList(), emptyList());
    private final PriceFinder priceFinder = PriceFinder.of(Monetary.getCurrency("EUR"), CountryCode.DE, Optional.empty(), Optional.empty());
    private final PriceFormatter priceFormatter = PriceFormatter.of(Locale.GERMAN);

    @Test
    public void create() {
        final ProductProjection jacket = readObjectFromResource("product.json", ProductProjection.typeReference());

        final ProductThumbnailData thumbnailData =
                ProductThumbnailDataFactory.of(translator, priceFinder, priceFormatter).create(jacket);

        assertThat(thumbnailData.getText()).isEqualTo("Freizeitjacke Save the Duck oliv");
        assertThat(thumbnailData.getDescription()).isEqualTo("german test description");
        assertThat(thumbnailData.getImage()).isEqualTo("https://s3-eu-west-1.amazonaws.com/commercetools-maximilian/products/078686_1_large.jpg");
        assertThat(thumbnailData.getPrice()).isEqualTo("EUR 129,00");
    }
}
