package common.pages;

import common.utils.Translator;
import io.sphere.sdk.categories.Category;

public class CategoryLinkDataFactory {

    private final Translator translator;

    private CategoryLinkDataFactory(final Translator translator) {
        this.translator = translator;
    }

    public static CategoryLinkDataFactory of(final Translator translator) {
        return new CategoryLinkDataFactory(translator);
    }

    public LinkData create(final Category category) {
        final String label = translator.translate(category.getName());
        return new LinkData(label, "");
    }
}
