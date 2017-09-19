package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.ctp.products.ProductAttributesSettings;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.content.products.AttributeWithProductType;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductAttributeViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductAttributeViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;

import static java.util.stream.Collectors.toList;

@RequestScoped
public class LineItemExtendedViewModelFactory extends AbstractLineItemViewModelFactory<LineItemExtendedViewModel> {

    private final ProductAttributesSettings productAttributesSettings;
    private final ProductAttributeViewModelFactory productAttributeViewModelFactory;

    @Inject
    public LineItemExtendedViewModelFactory(final PriceFormatter priceFormatter, final LineItemProductVariantViewModelFactory lineItemProductVariantViewModelFactory,
                                            final ProductAttributesSettings productAttributeSettings, final ProductAttributeViewModelFactory productAttributeViewModelFactory) {
        super(priceFormatter, lineItemProductVariantViewModelFactory);
        this.productAttributesSettings = productAttributeSettings;
        this.productAttributeViewModelFactory = productAttributeViewModelFactory;
    }

    protected final ProductAttributesSettings getProductAttributesSettings() {
        return productAttributesSettings;
    }

    protected final ProductAttributeViewModelFactory getProductAttributeViewModelFactory() {
        return productAttributeViewModelFactory;
    }

    @Override
    protected LineItemExtendedViewModel newViewModelInstance(final LineItem lineItem) {
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

    protected void fillAttributes(final LineItemExtendedViewModel viewModel, final LineItem lineItem) {
        viewModel.setAttributes(lineItem.getVariant().getAttributes().stream()
                .filter(attr -> productAttributesSettings.selectable().contains(attr.getName()))
                .map(attribute -> createProductAttributeViewModel(lineItem, attribute))
                .collect(toList()));
    }

    private ProductAttributeViewModel createProductAttributeViewModel(final LineItem lineItem, final Attribute attribute) {
        return productAttributeViewModelFactory.create(AttributeWithProductType.of(attribute, lineItem.getProductType()));
    }
}
