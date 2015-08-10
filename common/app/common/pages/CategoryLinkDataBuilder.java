package common.pages;

import common.utils.Translator;
import io.sphere.sdk.categories.Category;

public class CategoryLinkDataBuilder {

    private final Translator translator;

    private CategoryLinkDataBuilder(final Translator translator) {
        this.translator = translator;
    }

    public static CategoryLinkDataBuilder of(final Translator translator) {
        return new CategoryLinkDataBuilder(translator);
    }

    public LinkData build(final Category category) {
        final String label = translator.translate(category.getName());
        return new LinkData(label, "");
    }
}
