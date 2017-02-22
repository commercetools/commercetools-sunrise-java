package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.common.models.products.ProductAttributeViewModel;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.common.models.AttributeWithProductType;
import com.commercetools.sunrise.common.models.products.ProductAttributeViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class LineItemExtendedViewModelFactory extends AbstractLineItemViewModelFactory<LineItemExtendedViewModel> {

    private final ProductAttributeSettings productAttributeSettings;
    private final ProductAttributeViewModelFactory productAttributeViewModelFactory;

    @Inject
    public LineItemExtendedViewModelFactory(final PriceFormatter priceFormatter, final LineItemProductVariantViewModelFactory lineItemProductVariantViewModelFactory,
                                            final ProductAttributeSettings productAttributeSettings, final ProductAttributeViewModelFactory productAttributeViewModelFactory) {
        super(priceFormatter, lineItemProductVariantViewModelFactory);
        this.productAttributeSettings = productAttributeSettings;
        this.productAttributeViewModelFactory = productAttributeViewModelFactory;
    }

    @Override
    protected LineItemExtendedViewModel getViewModelInstance() {
        return new LineItemExtendedViewModel();
    }

    @Override
    public final LineItemExtendedViewModel create(final LineItem lineItem) {
        return super.create(lineItem);
    }

    @Override
    protected final void initialize(final LineItemExtendedViewModel viewModel, final LineItem lineItem) {
        super.initialize(viewModel, lineItem);
        fillAttributes(viewModel, lineItem);
    }

    protected void fillAttributes(final LineItemExtendedViewModel model, final LineItem lineItem) {
        final List<ProductAttributeViewModel> attributes = lineItem.getVariant().getAttributes().stream()
                .filter(attr -> productAttributeSettings.getSelectableAttributes().contains(attr.getName()))
                .map(attribute -> createProductAttributeViewModel(lineItem, attribute))
                .collect(toList());
        model.setAttributes(attributes);
    }

    private ProductAttributeViewModel createProductAttributeViewModel(final LineItem lineItem, final Attribute attribute) {
        return productAttributeViewModelFactory.create(AttributeWithProductType.of(attribute, lineItem.getProductType()));
    }
}
