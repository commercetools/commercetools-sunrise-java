package productcatalog.pages;

import com.neovisionaries.i18n.CountryCode;
import common.categories.CategoryUtils;
import common.contexts.UserContext;
import common.models.LinkData;
import common.pages.ReverseRouter;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import org.junit.Test;
import play.mvc.Call;

import static java.util.Collections.singletonList;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;

public class BreadcrumbDataFactoryTest {
    private final CategoryTree categories = CategoryTree.of(CategoryUtils.getQueryResult("categories.json").getResults());

    @Test
    public void create() {
        final Category category = categories.findById("5ebe6dc9-ba32-4030-9f3e-eee0137a1274").get();
        final BreadcrumbData breadcrumbData = new BreadcrumbData(category, categories, userContext(), reverseRouter());

        assertThat(breadcrumbData.getLinks()).hasSize(1);
        final LinkData linkData = breadcrumbData.getLinks().get(0);

        assertThat(linkData.getText()).isEqualTo("TestSnowboard equipment");
        assertThat(linkData.getUrl()).isEqualTo("");
    }

//    @Test
//    public void getBreadCrumbCategories() {
//        final ProductProjection suttonBag = getProductById(products, "254f1c0e-67bc-4aa6-992d-9a0fea1846b5");
//        final Category woman = categories.findById("33339d11-0e7b-406b-899b-60f4c34c2948").get();
//        final Category bags = categories.findById("32952779-d916-4f2b-b1d5-9efd7f7b9f58").get();
//        final Category handBags = categories.findById("9a584ee8-a45a-44e8-b9ec-e11439084687").get();
//
//        final List<Category> result = suttonBag.getCategories().stream().findFirst()
//                .map(categrieService::getBreadCrumbCategories)
//                .orElse(Collections.<Category>emptyList());
//
//        assertThat(result).containsExactly(woman, bags, handBags);
//    }

    private UserContext userContext() {
        return UserContext.of(CountryCode.UK, singletonList(ENGLISH), null, null);
    }

    private ReverseRouter reverseRouter() {
        return new ReverseRouter() {
            @Override
            public Call home(final String languageTag) {
                return null;
            }

            @Override
            public Call category(final String languageTag, final String categorySlug, final int page) {
                return new Call() {
                    @Override
                    public String url() {
                        return "some-url";
                    }

                    @Override
                    public String method() {
                        return null;
                    }

                    @Override
                    public String fragment() {
                        return null;
                    }
                };
            }

            @Override
            public Call category(final String languageTag, final String categorySlug) {
                return new Call() {
                    @Override
                    public String url() {
                        return "some-url";
                    }

                    @Override
                    public String method() {
                        return null;
                    }

                    @Override
                    public String fragment() {
                        return null;
                    }
                };
            }

            @Override
            public Call search(final String languageTag, final String searchTerm, final int page) {
                return null;
            }

            @Override
            public Call search(final String languageTag, final String searchTerm) {
                return null;
            }

            @Override
            public Call product(final String locale, final String productSlug, final String sku) {
                return null;
            }

            @Override
            public Call productVariantToCartForm(final String languageTag) {
                return null;
            }
        };
    }
}
