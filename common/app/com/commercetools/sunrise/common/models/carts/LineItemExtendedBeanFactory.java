package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.common.models.AttributeWithProductType;
import com.commercetools.sunrise.common.models.products.ProductAttributeBean;
import com.commercetools.sunrise.common.models.products.ProductAttributeBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class LineItemExtendedBeanFactory extends AbstractLineItemBeanFactory<LineItemExtendedBean> {

    private final ProductAttributeSettings productAttributeSettings;
    private final ProductAttributeBeanFactory productAttributeBeanFactory;

    @Inject
    public LineItemExtendedBeanFactory(final PriceFormatter priceFormatter, final LineItemProductVariantBeanFactory lineItemProductVariantBeanFactory,
                                       final ProductAttributeSettings productAttributeSettings, final ProductAttributeBeanFactory productAttributeBeanFactory) {
        super(priceFormatter, lineItemProductVariantBeanFactory);
        this.productAttributeSettings = productAttributeSettings;
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
                .filter(attr -> productAttributeSettings.getSelectableAttributes().contains(attr.getName()))
                .map(attribute -> createProductAttributeBean(lineItem, attribute))
                .collect(toList());
        model.setAttributes(attributes);
    }

    private ProductAttributeBean createProductAttributeBean(final LineItem lineItem, final Attribute attribute) {
        return productAttributeBeanFactory.create(AttributeWithProductType.of(attribute, lineItem.getProductType()));
    }
}
