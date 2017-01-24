package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.ProductAttributeBean;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactory;
import com.commercetools.sunrise.common.models.ProductVariantBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class LineItemExtendedBeanFactory extends AbstractLineItemBeanFactory<LineItemExtendedBean> {

    private final ProductDataConfig productDataConfig;
    private final ProductAttributeBeanFactory productAttributeBeanFactory;

    @Inject
    public LineItemExtendedBeanFactory(final PriceFormatter priceFormatter, final ProductDataConfig productDataConfig,
                                       final ProductVariantBeanFactory productVariantBeanFactory, final ProductAttributeBeanFactory productAttributeBeanFactory) {
        super(priceFormatter, productVariantBeanFactory);
        this.productDataConfig = productDataConfig;
        this.productAttributeBeanFactory = productAttributeBeanFactory;
    }

    public final LineItemExtendedBean create(final LineItem lineItem) {
        final Data data = new Data(lineItem);
        return initializedViewModel(data);
    }

    @Override
    protected LineItemExtendedBean getViewModelInstance() {
        return new LineItemExtendedBean();
    }

    @Override
    protected final void initialize(final LineItemExtendedBean bean, final Data data) {
        super.initialize(bean, data);
        fillAttributes(bean, data);
    }

    protected void fillAttributes(final LineItemExtendedBean bean, final Data data) {
        final List<ProductAttributeBean> attributes = data.lineItem.getVariant().getAttributes().stream()
                .filter(attr -> productDataConfig.getSelectableAttributes().contains(attr.getName()))
                .map(productAttributeBeanFactory::create)
                .collect(toList());
        bean.setAttributes(attributes);
    }
}
