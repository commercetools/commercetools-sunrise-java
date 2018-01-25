package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.categories.queries.CategoryQueryModel;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.queries.QueryPredicate;
import play.i18n.Lang;
import play.i18n.Langs;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

public final class DefaultCategoryFetcher extends AbstractCategoryFetcher {

    private final Langs langs;
    private final Locale locale;

    @Inject
    DefaultCategoryFetcher(final HookRunner hookRunner, final SphereClient sphereClient,
                           final Langs langs, final Locale locale) {
        super(hookRunner, sphereClient);
        this.langs = langs;
        this.locale = locale;
    }

    @Override
    protected Optional<CategoryQuery> buildRequest(final String slug) {
        return buildSlugPredicate(slug)
                .map(slugPredicate -> CategoryQuery.of().withPredicates(slugPredicate));
    }

    private Optional<QueryPredicate<Category>> buildSlugPredicate(final String slug) {
        return langs.availables().parallelStream()
                .map(lang -> Optional.of(buildSingleSlugPredicate(slug, lang)))
                .reduce(Optional.empty(), (opt1, opt2) ->
                        opt1.map(model1 -> Optional.of(opt2.map(model1::or).orElse(model1))).orElse(opt2));
    }

    private QueryPredicate<Category> buildSingleSlugPredicate(final String slug, final Lang lang) {
        return CategoryQueryModel.of().slug().lang(lang.toLocale()).is(slug);
    }

    @Override
    protected Optional<Category> selectResult(final PagedQueryResult<Category> results, final String slug) {
        if (results.getTotal() > 1) {
            return results.getResults().stream()
                    .filter(product -> productMatchesSlugInUsersLanguage(product, slug))
                    .findAny();
        } else {
            return results.head();
        }
    }

    private boolean productMatchesSlugInUsersLanguage(final Category category, final String slug) {
        return category.getSlug().find(locale)
                .map(slugInUsersLang -> slugInUsersLang.equals(slug))
                .orElse(false);
    }
}
