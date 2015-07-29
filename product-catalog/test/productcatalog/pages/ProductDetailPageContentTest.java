package productcatalog.pages;

import common.utils.Translator;
import io.sphere.sdk.json.JsonUtils;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import org.junit.Test;

import java.util.Locale;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductDetailPageContentTest {

    private final Translator translator = Translator.of(Locale.GERMAN, emptyList(), emptyList());

    private final ProductProjection product =
            JsonUtils.readObjectFromResource("productProjectionQuery.json", ProductProjectionQuery.resultTypeReference()).head().get();
    @Test
    public void additionalTitle() throws Exception {
        final ProductDetailPageContent content = new ProductDetailPageContent(null, translator, null, null, product, null, null, null, null);

        assertThat(content.additionalTitle()).isEqualTo("Tasche „Sutton” large Michael Kors");
    }
}
