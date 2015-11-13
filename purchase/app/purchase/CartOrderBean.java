package purchase;

import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;

public class CartOrderBean {
    private Long itemsTotal;

    public CartOrderBean() {
    }

    public CartOrderBean(final CartLike<?> cartLike) {
        this();
        setItemsTotal(cartLike.getLineItems().stream().mapToLong(LineItem::getQuantity).sum());
    }

    public Long getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(final Long itemsTotal) {
        this.itemsTotal = itemsTotal;
    }
}
