package com.commercetools.sunrise;

import com.google.inject.AbstractModule;
import com.neovisionaries.i18n.CountryCode;
import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.models.ProductDataConfig;
import io.sphere.sdk.producttypes.MetaProductType;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.queries.ProductTypeQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import javax.money.Monetary;
import java.util.Locale;

import static com.commercetools.sunrise.common.utils.JsonUtils.readCtpObject;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

public class CtpModelsTestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ProjectContext.class).toInstance(injectedProjectContext());
        bind(ProductDataConfig.class).toInstance(injectedProductDataConfig());
    }

    protected ProjectContext injectedProjectContext() {
        return ProjectContext.of(
                asList(Locale.ENGLISH, Locale.GERMAN),
                asList(CountryCode.US, CountryCode.DE),
                asList(Monetary.getCurrency("USD"), Monetary.getCurrency("EUR")));
    }

    protected ProductDataConfig injectedProductDataConfig() {
        final PagedQueryResult<ProductType> result = readCtpObject("data/product-types.json", ProductTypeQuery.resultTypeReference());
        final MetaProductType metaProductType = MetaProductType.of(result.getResults());
        return ProductDataConfig.of(metaProductType, asList("foo", "bar"), singletonList("foo"), singletonList("bar"));
    }
}