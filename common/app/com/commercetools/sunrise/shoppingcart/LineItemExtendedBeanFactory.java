package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.LineItemProductVariantBeanFactory;
import com.commercetools.sunrise.common.models.ProductAttributeBean;
import com.commercetools.sunrise.common.models.ProductAttributeBeanFactory;
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
    public LineItemExtendedBeanFactory(final PriceFormatter priceFormatter, final LineItemProductVariantBeanFactory lineItemProductVariantBeanFactory,
                                       final ProductDataConfig productDataConfig, final ProductAttributeBeanFactory productAttributeBeanFactory) {
        super(priceFormatter, lineItemProductVariantBeanFactory);
        this.productDataConfig = productDataConfig;
        this.productAttributeBeanFactory = productAttributeBeanFactory;
    }

    @Override
    protected LineItemExtendedBean getViewModelInstance() {
        return new LineItemExtendedBean();
    }

    @Override
    public final LineItemExtendedBean create(final LineItem data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final LineItemExtendedBean model, final LineItem data) {
        super.initialize(model, data);
        fillAttributes(model, data);
    }

    protected void fillAttributes(final LineItemExtendedBean model, final LineItem lineItem) {
        final List<ProductAttributeBean> attributes = lineItem.getVariant().getAttributes().stream()
                .filter(attr -> productDataConfig.getSelectableAttributes().contains(attr.getName()))
                .map(productAttributeBeanFactory::create)
                .collect(toList());
        model.setAttributes(attributes);
    }
}
