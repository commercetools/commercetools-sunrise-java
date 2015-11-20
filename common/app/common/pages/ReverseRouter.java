package common.pages;

import play.mvc.Call;

public interface ReverseRouter {

    Call home(final String languageTag);

    Call category(final String languageTag, final String categorySlug, final int page);

    Call category(final String languageTag, final String categorySlug);

    Call search(final String languageTag, final String searchTerm, final int page);

    Call search(final String languageTag, final String searchTerm);

    Call product(final String locale, final String productSlug, final String sku);

    Call productVariantToCartForm(final String languageTag);

}
