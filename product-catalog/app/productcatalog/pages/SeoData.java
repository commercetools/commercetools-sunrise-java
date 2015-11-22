package productcatalog.pages;

import common.contexts.UserContext;
import common.models.DetailData;
import io.sphere.sdk.categories.Category;

import java.util.Optional;

public class SeoData extends DetailData {

    public SeoData(final UserContext userContext, final Category category) {
        Optional.ofNullable(category.getMetaTitle())
                .ifPresent(title -> setTitle(title.find(userContext.locales()).orElse("")));
        Optional.ofNullable(category.getMetaDescription())
                .ifPresent(description -> setTitle(description.find(userContext.locales()).orElse("")));
    }
}
