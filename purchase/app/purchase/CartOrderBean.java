package purchase;

import common.contexts.UserContext;
import common.models.ProductDataConfig;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.carts.TaxedPrice;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.utils.MoneyImpl;

import javax.money.MonetaryAmount;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class CartOrderBean {
    private Long totalItems;
    private String salesTax;
    private String totalPrice;
    private String subtotalPrice;
    private LineItemsBean lineItems;
    private AddressBean shippingAddress;
    private AddressBean billingAddress;
    private SelectableShippingMethodBean shippingMethod;
    private PaymentsBean paymentDetails;
    private String orderNumber;
    private String orderDate;
    private String customerEmail;

    public CartOrderBean() {
    }

    public CartOrderBean(final CartLike<?> cartLike, final UserContext userContext, final ProductDataConfig productDataConfig) {
        this();
        final MoneyContext moneyContext = MoneyContext.of(cartLike, userContext);

        final long itemsTotal = cartLike.getLineItems().stream().mapToLong(LineItem::getQuantity).sum();
        setTotalItems(itemsTotal);
        final Optional<TaxedPrice> taxedPriceOptional = Optional.ofNullable(cartLike.getTaxedPrice());
        final MonetaryAmount tax = taxedPriceOptional.map(CartOrderBean::calculateTax).orElse(moneyContext.zero());
        setSalesTax(moneyContext.formatOrZero(tax));

        final MonetaryAmount totalPrice = cartLike.getTotalPrice();
        final MonetaryAmount orderTotal = taxedPriceOptional.map(TaxedPrice::getTotalGross).orElse(totalPrice);
        setTotalPrice(moneyContext.formatOrZero(orderTotal));

        final MonetaryAmount subTotal = calculateSubTotal(cartLike.getLineItems(), totalPrice);
        setSubtotalPrice(moneyContext.formatOrZero(subTotal));

        setLineItems(new LineItemsBean(cartLike, userContext, productDataConfig));

        setShippingAddress(new AddressBean(cartLike.getShippingAddress(), userContext.locale()));
        setBillingAddress(new AddressBean(cartLike.getBillingAddress(), userContext.locale()));

        final PaymentsBean paymentDetails = new PaymentsBean();
        paymentDetails.setType("prepaid");
        setPaymentDetails(paymentDetails);

        setShippingMethod(new SelectableShippingMethodBean(cartLike, moneyContext));

        if (cartLike instanceof Order) {
            fillOrderStuff((Order) cartLike, userContext);
        }
    }

    private void fillOrderStuff(final Order order, final UserContext userContext) {
        final String email = Optional.ofNullable(order.getCustomerEmail())
                .orElseGet(() -> order.getShippingAddress().getEmail());
        setCustomerEmail(email);

        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", userContext.locale());
        setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));

        setOrderNumber(order.getOrderNumber());
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



    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(final Long totalItems) {
        this.totalItems = totalItems;
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

    public String getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setSubtotalPrice(final String subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public AddressBean getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(final AddressBean billingAddress) {
        this.billingAddress = billingAddress;
    }

    public AddressBean getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(final AddressBean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public SelectableShippingMethodBean getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(final SelectableShippingMethodBean shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public PaymentsBean getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(final PaymentsBean paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(final String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
