package productcatalog.models;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.UserContext;
import common.models.DetailData;
import common.models.SelectableData;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import org.junit.Test;

import javax.money.Monetary;
import java.util.List;

import static io.sphere.sdk.json.SphereJsonUtils.readObjectFromResource;
import static java.util.Collections.singletonList;
import static java.util.Locale.GERMAN;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDataFactoryTest {
    public static final UserContext USER_CONTEXT =  UserContext.of(CountryCode.DE, singletonList(GERMAN), null, Monetary.getCurrency("EUR"));

//    @Test
//    public void create() {
//        final ProductProjection product = readObjectFromResource("product.json", ProductProjection.typeReference());
//        final ProductVariant variant = product.getMasterVariant();
//
//        final ProductData productData = ProductDataFactory.of(USER_CONTEXT).create(product, variant);
//        final List<String> colors = productData.getColors().stream().map(SelectableData::getText).collect(toList());
//        final List<String> sizes = productData.getSizes().stream().map(SelectableData::getText).collect(toList());
//        final List<String> details = productData.getDetails().stream().map(DetailData::getText).collect(toList());
//
//        assertThat(productData.getText()).isEqualTo("Freizeitjacke Save the Duck oliv");
//        assertThat(productData.getSku()).isEqualTo("M0E20000000DSB9");
//        assertThat(productData.getDescription()).isEqualTo("german test description");
//        assertThat(productData.getPrice()).isEqualTo("EUR 100,-");
//        assertThat(productData.getPriceOld()).isEqualTo("EUR 129,-");
//        assertThat(colors).containsExactly("oliv");
//        assertThat(sizes).containsExactly("XXS", "XS", "S", "M", "L", "XL", "XXL", "XXXL");
//        assertThat(details).containsExactly("detail de 2", "detail de 1");
//    }
}
