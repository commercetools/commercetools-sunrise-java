package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.queries.ProductProjectionQueryModel;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.QueryPredicate;
import play.i18n.Lang;
import play.i18n.Langs;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

public final class DefaultProductFetcher extends AbstractProductFetcher {

    private final Locale locale;
    private final Langs langs;
    private final PriceSelection priceSelection;

    @Inject
    DefaultProductFetcher(final HookRunner hookRunner, final SphereClient sphereClient,
                          final Locale locale, final Langs langs, final PriceSelection priceSelection) {
        super(hookRunner, sphereClient);
        this.locale = locale;
        this.langs = langs;
        this.priceSelection = priceSelection;
    }

    @Override
    protected Optional<ProductProjectionQuery> buildRequest(final String slug, final String sku) {
        return buildSlugPredicate(slug)
                .map(slugPredicate -> ProductProjectionQuery.ofCurrent()
                        .withPriceSelection(priceSelection)
                        .withPredicates(slugPredicate));
    }

    private Optional<QueryPredicate<ProductProjection>> buildSlugPredicate(final String slug) {
        return langs.availables().parallelStream()
                .map(lang -> Optional.of(buildSingleSlugPredicate(slug, lang)))
                .reduce(Optional.empty(), (opt1, opt2) ->
                        opt1.map(model1 -> Optional.of(opt2.map(model1::or).orElse(model1))).orElse(opt2));
    }

    private QueryPredicate<ProductProjection> buildSingleSlugPredicate(final String slug, final Lang lang) {
        return ProductProjectionQueryModel.of().slug().lang(lang.toLocale()).is(slug);
    }

    @Override
    protected Optional<ProductWithVariant> selectResult(final PagedQueryResult<ProductProjection> result,
                                                        final String slug, @Nullable final String sku) {
        return selectProduct(result, slug)
                .map(product -> ProductWithVariant.of(product, selectVariant(product, sku)));
    }

    private Optional<ProductProjection> selectProduct(final PagedQueryResult<ProductProjection> pagedResult, final String slug) {
        if (pagedResult.getTotal() > 1) {
            return pagedResult.getResults().stream()
                    .filter(product -> productMatchesSlugInUsersLanguage(product, slug))
                    .findAny();
        }
        return pagedResult.head();
    }

    private ProductVariant selectVariant(final ProductProjection product, final String sku) {
        return product.findVariantBySku(sku).orElseGet(product::getMasterVariant);
    }

    private boolean productMatchesSlugInUsersLanguage(final ProductProjection product, final String slug) {
        return product.getSlug().find(locale)
                .map(slugInUsersLang -> slugInUsersLang.equals(slug))
                .orElse(false);
    }
}
