package productcatalog.productsuggestions;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.controllers.SunrisePageData;
import common.hooks.SunrisePageDataHook;
import common.models.ProductDataConfig;
import common.suggestion.ProductRecommendation;
import framework.ControllerComponent;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import io.sphere.sdk.products.ProductProjection;
import play.Configuration;
import productcatalog.common.ProductListBean;
import productcatalog.common.ProductListBeanFactory;
import productcatalog.common.SuggestionsData;
import productcatalog.hooks.SingleProductProjectionHook;
import productcatalog.productdetail.ProductDetailPageContent;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

public class ProductSuggestionsControllerComponent implements ControllerComponent, SingleProductProjectionHook, SunrisePageDataHook {

    @Inject
    private UserContext userContext;
    @Inject
    private ProductRecommendation productRecommendation;
    @Inject
    private CategoryTree categoryTree;
    @Inject
    private ProductDataConfig productDataConfig;
    @Inject
    private ReverseRouter reverseRouter;//TODO framework remove this big dep
    @Inject
    private ProductListBeanFactory productListBeanFactory;
    private int numSuggestions;
    private Set<ProductProjection> suggestions;
    private String categoryNewExtId;

    @Inject
    public void setConfig(final Configuration configuration) {
        this.numSuggestions = configuration.getInt("pdp.productSuggestions.count");
        this.categoryNewExtId = configuration.getString("common.newCategoryExternalId");
    }

    @Override
    public CompletionStage<?> onSingleProductProjectionLoaded(final ProductProjection product) {
        return productRecommendation.relatedToProduct(product, numSuggestions)
                .thenAccept(m -> suggestions = m);
    }

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        if (sunrisePageData.getContent() instanceof ProductDetailPageContent) {
            final ProductDetailPageContent content = (ProductDetailPageContent) sunrisePageData.getContent();
            content.setSuggestions(createSuggestions(userContext, suggestions));
        }
    }

    private SuggestionsData createSuggestions(final UserContext userContext, final Set<ProductProjection> suggestions) {
        final ProductListBean productListData = productListBeanFactory.create(suggestions, categoryTreeInNew());
        return new SuggestionsData(productListData);
    }

    //TODO duplicated
    private CategoryTree categoryTreeInNew() {
        final List<Category> categoriesInNew = newCategory()
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
        return categoryTree.getSubtree(categoriesInNew);
    }

    //TODO duplicated
    private Optional<Category> newCategory() {
        return Optional.ofNullable(categoryNewExtId).flatMap(extId -> categoryTree.findByExternalId(extId));
    }
}
