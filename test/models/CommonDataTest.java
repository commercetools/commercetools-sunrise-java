package models;

import com.neovisionaries.i18n.CountryCode;
import common.models.CommonDataBuilder;
import common.models.UserContext;
import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.products.*;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeBuilder;
import org.junit.Test;
import play.i18n.Lang;

import java.util.Arrays;
import java.util.List;

import static java.util.Collections.emptyList;
import static org.fest.assertions.Assertions.assertThat;

public class CommonDataTest {

    @Test
    public void hasProvidedProductList() {
        List<Product> productList = Arrays.asList(defaultProduct());
        CommonDataBuilder builder = CommonDataBuilder.of(defaultContext(), emptyList()).products(productList);
        assertThat(builder.build().products()).isEqualTo(productList);
    }

    @Test
    public void hasEmptyProductListWhenNoneProvided() {
        CommonDataBuilder builder = CommonDataBuilder.of(defaultContext(), emptyList());
        assertThat(builder.build().products()).isEmpty();
    }

    private UserContext defaultContext() {
        return UserContext.of(Lang.forCode("de"), CountryCode.DE);
    }

    private Product defaultProduct() {
        ProductType productType = ProductTypeBuilder.of("id", "product-type", "", emptyList()).build();
        LocalizedStrings locString = LocalizedStrings.ofEnglishLocale("text");
        ProductVariant variant = ProductVariantBuilder.of(1).build();
        ProductData productData = ProductDataBuilder.of(locString, locString, variant).build();
        ProductCatalogData catalogData = ProductCatalogDataBuilder.ofStaged(productData).build();
        return ProductBuilder.of(productType, catalogData).build();
    }
}
