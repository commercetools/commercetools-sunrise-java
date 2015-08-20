package productcatalog.pages;

import com.neovisionaries.i18n.CountryCode;
import common.pages.DetailData;
import common.pages.SelectableData;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import org.junit.Test;

import javax.money.Monetary;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDataFactoryTest {
    private final Translator translator = Translator.of(Locale.GERMAN, emptyList(), emptyList());
    private final PriceFinder priceFinder = PriceFinder.of(Monetary.getCurrency("EUR"), CountryCode.DE, Optional.empty(), Optional.empty());
    private final PriceFormatter priceFormatter = PriceFormatter.of(Locale.GERMAN);

    @Test
    public void create() {
        final ProductProjection product = readObjectFromResource("product.json", ProductProjection.typeReference());
        final ProductVariant variant = product.getMasterVariant();

        final ProductData productData = ProductDataFactory.of(translator, priceFinder, priceFormatter).create(product, variant);
        final List<String> colors = productData.getColor().stream().map(SelectableData::getText).collect(toList());
        final List<String> sizes = productData.getSize().stream().map(SelectableData::getText).collect(toList());
        final List<String> details = productData.getProductDetails().stream().map(DetailData::getText).collect(toList());

        assertThat(productData.getText()).isEqualTo("Freizeitjacke Save the Duck oliv");
        assertThat(productData.getSku()).isEqualTo("M0E20000000DSB9");
        assertThat(productData.getDescription()).isEqualTo("german test description");
        assertThat(productData.getPrice()).isEqualTo("EUR 100,00");
        assertThat(productData.getPriceOld()).isEqualTo("EUR 129,00");
        assertThat(colors).containsExactly("oliv");
        assertThat(sizes).containsExactly("XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL");
        assertThat(details).containsExactly("detail de 2", "detail de 1");
    }
}
