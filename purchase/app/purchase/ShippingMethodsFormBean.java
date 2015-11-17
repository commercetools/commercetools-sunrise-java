package purchase;

import io.sphere.sdk.carts.Cart;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class ShippingMethodsFormBean {
    private List<SelectableShippingMethodBean> list;


    public ShippingMethodsFormBean() {
    }

    public ShippingMethodsFormBean(final Cart cart, final ShippingMethods shippingMethods) {
        final List<SelectableShippingMethodBean> shippingMethodBeanList = shippingMethods.shippingMethods.stream()
                .map(shippingMethod -> {
                    final SelectableShippingMethodBean bean = new SelectableShippingMethodBean();
                    bean.setText(shippingMethod.getName());
                    bean.setName(shippingMethod.getName());
                    bean.setValue(shippingMethod.getId());
                    final Boolean selected = Optional.ofNullable(cart.getShippingInfo())
                            .map(s -> s.getShippingMethod())
                            .map(r -> r.getId().equals(shippingMethod.getId()))
                            .orElse(false);
                    bean.setSelected(selected);
                    return bean;
                })
                .collect(toList());
        setList(shippingMethodBeanList);
    }

    public List<SelectableShippingMethodBean> getList() {
        return list;
    }

    public void setList(final List<SelectableShippingMethodBean> list) {
        this.list = list;
    }
}
