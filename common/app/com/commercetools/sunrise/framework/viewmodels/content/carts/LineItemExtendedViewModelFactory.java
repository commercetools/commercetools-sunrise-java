package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.framework.viewmodels.content.products.AttributeWithProductType;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductAttributeViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.products.ProductAttributeViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;

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

    protected final ProductAttributeSettings getProductAttributeSettings() {
        return productAttributeSettings;
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
                .filter(attr -> productAttributeSettings.getSelectableAttributes().contains(attr.getName()))
                .map(attribute -> createProductAttributeViewModel(lineItem, attribute))
                .collect(toList()));
    }

    private ProductAttributeViewModel createProductAttributeViewModel(final LineItem lineItem, final Attribute attribute) {
        return productAttributeViewModelFactory.create(AttributeWithProductType.of(attribute, lineItem.getProductType()));
    }
}
