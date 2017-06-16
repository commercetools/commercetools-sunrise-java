package com.commercetools.sunrise.framework.viewmodels.content.products;

import com.commercetools.sunrise.ctp.ProductAttributeSettings;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;

import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
public class ProductViewModelFactory extends SimpleViewModelFactory<ProductViewModel, ProductWithVariant> {

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

    protected final ProductAttributeSettings getProductAttributeSettings() {
        return productAttributeSettings;
    }

    protected final ProductVariantViewModelFactory getProductVariantViewModelFactory() {
        return productVariantViewModelFactory;
    }

    protected final ProductDetailsViewModelFactory getProductDetailsViewModelFactory() {
        return productDetailsViewModelFactory;
    }

    protected final ProductGalleryViewModelFactory getProductGalleryViewModelFactory() {
        return productGalleryViewModelFactory;
    }

    protected final SelectableProductAttributeViewModelFactory getSelectableProductAttributeViewModelFactory() {
        return selectableProductAttributeViewModelFactory;
    }

    protected final ProductVariantReferenceViewModelMapFactory getProductVariantReferenceViewModelMapFactory() {
        return productVariantReferenceViewModelMapFactory;
    }

    @Override
    protected ProductViewModel newViewModelInstance(final ProductWithVariant productWithVariant) {
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

    protected void fillProductId(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setProductId(productWithVariant.getProduct().getId());
    }

    protected void fillVariantId(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setVariantId(productWithVariant.getVariant().getId());
    }

    protected void fillDescription(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setDescription(productWithVariant.getProduct().getDescription());
    }

    protected void fillGallery(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setGallery(productGalleryViewModelFactory.create(productWithVariant.getVariant()));
    }

    protected void fillDetails(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setDetails(productDetailsViewModelFactory.create(productWithVariant));
    }

    protected void fillVariant(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setVariant(productVariantViewModelFactory.create(productWithVariant));
    }

    protected void fillAttributes(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setAttributes(selectableProductAttributeViewModelFactory.createList(productWithVariant));
    }

    protected void fillAttributeCombination(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setVariants(productVariantReferenceViewModelMapFactory.create(productWithVariant));
    }

    protected void fillVariantIdentifiers(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
        viewModel.setVariantIdentifiers(productAttributeSettings.getSelectableAttributes());
    }

    protected void fillAvailability(final ProductViewModel viewModel, final ProductWithVariant productWithVariant) {
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
                    viewModel.setAvailability(status);
                });
    }
}
