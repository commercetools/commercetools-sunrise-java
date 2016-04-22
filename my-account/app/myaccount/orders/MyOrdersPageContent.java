package myaccount.orders;

import common.contexts.UserContext;
import common.controllers.PageContent;
import common.controllers.ReverseRouter;
import common.template.i18n.I18nResolver;
import io.sphere.sdk.orders.Order;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class MyOrdersPageContent extends PageContent {

    private List<OrderOverviewBean> order;

    public MyOrdersPageContent() {
    }

    public MyOrdersPageContent(final List<Order> orders, final UserContext userContext, final I18nResolver i18nResolver,
                               final ReverseRouter reverseRouter) {
        this.order = orders.stream()
                .map(order -> new OrderOverviewBean(order, userContext, i18nResolver, reverseRouter))
                .collect(toList());
    }

    public List<OrderOverviewBean> getOrder() {
        return order;
    }

    public void setOrder(final List<OrderOverviewBean> order) {
        this.order = order;
    }
}
