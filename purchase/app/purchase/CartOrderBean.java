package purchase;

import common.contexts.UserContext;
import common.models.*;
import common.utils.PriceFormatter;
import common.utils.ZeroPriceFormatter;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.carts.TaxedPrice;
import io.sphere.sdk.utils.MoneyImpl;

import javax.money.MonetaryAmount;
import java.util.List;
import java.util.Optional;

public class CartOrderBean {
    private Long itemsTotal;
    private String salesTax;
    private String total;
    private String subtotal;
    private LineItemsBean lineItems;

    public CartOrderBean() {
    }

    public CartOrderBean(final CartLike<?> cartLike, final UserContext userContext, final ProductDataConfig productDataConfig) {
        this();
        setItemsTotal(cartLike.getLineItems().stream().mapToLong(LineItem::getQuantity).sum());
        final ZeroPriceFormatter priceFormatter = PriceFormatter.of(userContext.locale(), userContext.currency());
        final Optional<TaxedPrice> taxedPriceOptional = Optional.ofNullable(cartLike.getTaxedPrice());
        final MonetaryAmount tax = taxedPriceOptional.map(CartOrderBean::calculateTax).orElse(null);
        setSalesTax(priceFormatter.formatOrZero(tax));

        final MonetaryAmount totalPrice = cartLike.getTotalPrice();
        final MonetaryAmount orderTotal = taxedPriceOptional.map(TaxedPrice::getTotalGross).orElse(totalPrice);
        setTotal(priceFormatter.format(orderTotal));

        final MonetaryAmount subTotal = calculateSubTotal(cartLike.getLineItems(), totalPrice);
        setSubtotal(priceFormatter.format(subTotal));

        setLineItems(new LineItemsBean(cartLike, userContext, productDataConfig));
    }

    private static MonetaryAmount calculateTax(final TaxedPrice taxedPrice) {
        return taxedPrice.getTotalGross().subtract(taxedPrice.getTotalNet());
    }

    private static MonetaryAmount calculateSubTotal(final List<LineItem> lineItems, final MonetaryAmount totalPrice) {
        final MonetaryAmount zeroAmount = MoneyImpl.ofCents(0, totalPrice.getCurrency());

        return lineItems
                .stream()
                .map(lineItem -> {
                    final MonetaryAmount amount = common.models.ProductVariantBean.calculateAmountForOneLineItem(lineItem);
                    final Long quantity = lineItem.getQuantity();
                    return amount.multiply(quantity);
                })
                .reduce(zeroAmount, (left, right) -> left.add(right));
    }



    public Long getItemsTotal() {
        return itemsTotal;
    }

    public void setItemsTotal(final Long itemsTotal) {
        this.itemsTotal = itemsTotal;
    }

    public LineItemsBean getLineItems() {
        return lineItems;
    }

    public void setLineItems(final LineItemsBean lineItems) {
        this.lineItems = lineItems;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(final String salesTax) {
        this.salesTax = salesTax;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(final String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(final String total) {
        this.total = total;
    }
}
