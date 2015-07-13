package productcatalog.pages;

import common.contexts.AppContext;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

public class SuggestionListData extends ProductListData {

    public SuggestionListData(List<ProductProjection> productList, AppContext context, PriceFormatter priceFormatter) {
        super(productList, context, priceFormatter);
    }

    public String getText() {
        return "You may also like";
    }
}
