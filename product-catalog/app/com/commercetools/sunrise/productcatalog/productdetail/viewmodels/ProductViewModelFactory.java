package com.commercetools.sunrise.productcatalog.productdetail.viewmodels;

import com.commercetools.sunrise.common.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.productcatalog.productdetail.ProductWithVariant;

import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class ProductViewModelFactory extends ViewModelFactory<ProductViewModel, ProductWithVariant> {

    private final ProductAttributeSettings productAttributeSettings;
    private final ProductVariantViewModelFactory productVariantViewModelFactory;
    private final ProductDetailsViewModelFactory productDetailsViewModelFactory;
    private final ProductGalleryViewModelFactory productGalleryViewModelFactory;
    private final SelectableProductAttributeViewModelFactory selectableProductAttributeViewModelFactory;
    private final ProductVariantReferenceViewModelMapFactory productVariantReferenceViewModelMapFactory;

    @Inject
    public ProductViewModelFactory(final ProductAttributeSettings productAttributeSettings, final ProductVariantViewModelFactory productVariantViewModelFactory,
                                   final ProductDetailsViewModelFactory productDetailsViewModelFactory, final ProductGalleryViewModelFactory productGalleryViewModelFactory,
                                   final SelectableProductAttributeViewModelFactory selectableProductAttributeViewModelFactory,
                                   final ProductVariantReferenceViewModelMapFactory productVariantReferenceViewModelMapFactory) {
        this.productAttributeSettings = productAttributeSettings;
        this.productVariantViewModelFactory = productVariantViewModelFactory;
        this.productDetailsViewModelFactory = productDetailsViewModelFactory;
        this.productGalleryViewModelFactory = productGalleryViewModelFactory;
        this.selectableProductAttributeViewModelFactory = selectableProductAttributeViewModelFactory;
        this.productVariantReferenceViewModelMapFactory = productVariantReferenceViewModelMapFactory;
    }

    @Override
    protected ProductViewModel getViewModelInstance() {
        return new ProductViewModel();
    }

    @Override
    public final ProductViewModel create(final ProductWithVariant productWithVariant) {
        return super.create(productWithVariant);
    }

    @Override
    protected final void initialize(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        fillProductId(viewModel, productWithVariant);
        fillVariantId(viewModel, productWithVariant);
        fillDescription(viewModel, productWithVariant);
        fillGallery(viewModel, productWithVariant);
        fillDetails(viewModel, productWithVariant);
        fillVariant(viewModel, productWithVariant);
        fillAttributes(viewModel, productWithVariant);
        fillAvailability(viewModel, productWithVariant);
        fillVariantIdentifiers(viewModel, productWithVariant);
        fillAttributeCombination(viewModel, productWithVariant);
    }

    protected void fillProductId(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        model.setProductId(productWithVariant.getProduct().getId());
    }

    protected void fillVariantId(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        model.setVariantId(productWithVariant.getVariant().getId());
    }

    protected void fillDescription(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        model.setDescription(productWithVariant.getProduct().getDescription());
    }

    protected void fillGallery(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        model.setGallery(productGalleryViewModelFactory.create(productWithVariant));
    }

    protected void fillDetails(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        model.setDetails(productDetailsViewModelFactory.create(productWithVariant));
    }

    protected void fillVariant(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        model.setVariant(productVariantViewModelFactory.create(productWithVariant));
    }

    protected void fillAttributes(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        model.setAttributes(selectableProductAttributeViewModelFactory.createList(productWithVariant));
    }

    protected void fillAttributeCombination(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        model.setVariants(productVariantReferenceViewModelMapFactory.create(productWithVariant));
    }

    protected void fillVariantIdentifiers(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        model.setVariantIdentifiers(productAttributeSettings.getSelectableAttributes());
    }

    protected void fillAvailability(final ProductViewModel model, final ProductWithVariant productWithVariant) {
        Optional.ofNullable(productWithVariant.getVariant().getAvailability())
                .flatMap(productVariantAvailability -> Optional.ofNullable(productVariantAvailability.getAvailableQuantity()))
                .ifPresent(quantity -> {
                    final String status;
                    if (quantity < 4) {
                        status = "notAvailable";
                    } else if (quantity > 10) {
                        status = "available";
                    } else {
                        status = "fewItemsLeft";
                    }
                    model.setAvailability(status);
                });
    }
}
