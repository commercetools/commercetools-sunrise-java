package common.pages;

import common.categories.CategoryUtils;
import common.utils.Translator;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import org.junit.Test;

import java.util.Locale;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryLinkDataFactoryTest {
    private final Translator translator = Translator.of(Locale.ENGLISH, emptyList(), emptyList());
    private final CategoryTree categories = CategoryTree.of(CategoryUtils.getQueryResult("categories.json").getResults());

    @Test
    public void create() {
        final Category category = categories.findById("5ebe6dc9-ba32-4030-9f3e-eee0137a1274").get();

        final LinkData linkData = CategoryLinkDataFactory.of(translator).create(category);

        assertThat(linkData.getText()).isEqualTo("TestSnowboard equipment");
        assertThat(linkData.getUrl()).isEqualTo("");
    }
}
