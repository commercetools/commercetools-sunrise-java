package purchase;

import common.contexts.UserContext;
import common.pages.PageContent;
import common.pages.ReverseRouter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;
import play.i18n.Messages;

public class CartDetailPageContent extends PageContent {
    private final CartOrderBean cart;

    public CartDetailPageContent(final Cart cart, final UserContext userContext, final Messages messages, final ReverseRouter reverseRouter) {
        this.cart = new CartOrderBean();
        final long totalItems = cart.getLineItems().stream().mapToLong(LineItem::getQuantity).sum();
        this.cart.setItemsTotal(totalItems);
    }

    @Override
    public String additionalTitle() {
        return "TODO ADDITIONAL TITLE";
    }
}
