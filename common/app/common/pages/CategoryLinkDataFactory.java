package common.pages;

import common.utils.TranslationResolver;
import io.sphere.sdk.categories.Category;

public class CategoryLinkDataFactory {

    private final TranslationResolver translationResolver;

    private CategoryLinkDataFactory(final TranslationResolver translationResolver) {
        this.translationResolver = translationResolver;
    }

    public static CategoryLinkDataFactory of(final TranslationResolver translationResolver) {
        return new CategoryLinkDataFactory(translationResolver);
    }

    public LinkData create(final Category category) {
        final String label = translationResolver.getTranslation(category.getName());
        return new LinkData(label, "");
    }
}
