package common.pages;

import common.categories.CategoryUtils;
import common.utils.TranslationResolver;
import common.utils.TranslationResolverImpl;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import org.junit.Test;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryLinkDataFactoryTest {
    private final TranslationResolver translationResolver = TranslationResolverImpl.of(Locale.ENGLISH);
    private final CategoryTree categories = CategoryTree.of(CategoryUtils.getQueryResult("categories.json").getResults());

    @Test
    public void create() {
        final Category category = categories.findById("5ebe6dc9-ba32-4030-9f3e-eee0137a1274").get();

        final LinkData linkData = CategoryLinkDataFactory.of(translationResolver).create(category);

        assertThat(linkData.getText()).isEqualTo("TestSnowboard equipment");
        assertThat(linkData.getUrl()).isEqualTo("");
    }
}
