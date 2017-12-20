package com.commercetools.sunrise.models.attributes;

import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.ViewModelFactory;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Singleton
public class AttributeOptionFactory extends ViewModelFactory {

    private final ProductAttributesSettings attributesSettings;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    protected AttributeOptionFactory(final ProductAttributesSettings attributesSettings,
                                     final ProductReverseRouter productReverseRouter) {
        this.attributesSettings = attributesSettings;
        this.productReverseRouter = productReverseRouter;
    }

    public AttributeOption create(final Attribute attribute, final ProductProjection product, final ProductVariant variant) {
        final AttributeOption viewModel = new AttributeOption(attribute);
        final boolean selected = isSelected(attribute, variant);
        viewModel.setSelected(selected);
        final ProductVariant targetVariant;
        if (selected) {
            targetVariant = variant;
        } else {
            final List<ProductVariant> candidates = findVariantCandidates(attribute, product);
            final Optional<ProductVariant> targetVariantOpt = findTargetVariant(candidates, attribute, variant);
            if (!targetVariantOpt.isPresent()) {
                viewModel.setDisabled(true);
            }
            targetVariant = targetVariantOpt.orElseGet(() -> candidates.stream()
                    .findFirst()
                    .orElseGet(product::getMasterVariant));
        }
        viewModel.setVariantId(targetVariant.getId());
        findUrl(product, targetVariant).ifPresent(viewModel::setVariantUrl);
        return viewModel;
    }

    private boolean isSelected(final Attribute attribute, final ProductVariant variant) {
        return attribute.equals(variant.getAttribute(attribute.getName()));
    }

    private Optional<String> findUrl(final ProductProjection product, final ProductVariant targetVariant) {
        return productReverseRouter.productDetailPageCall(product, targetVariant).map(Call::url);
    }

    private Optional<ProductVariant> findTargetVariant(final List<ProductVariant> candidates, final Attribute attribute, final ProductVariant variant) {
        return candidates.stream()
                .filter(candidate -> attributesSettings.selectable().stream()
                        .filter(attributeName -> !attributeName.equals(attribute.getName()))
                        .allMatch(attributeName -> Objects.equals(candidate.getAttribute(attributeName), variant.getAttribute(attributeName))))
                .findFirst();
    }

    private List<ProductVariant> findVariantCandidates(final Attribute attribute, final ProductProjection product) {
        return product.getAllVariants().stream()
                .filter(variant -> attribute.equals(variant.getAttribute(attribute.getName())))
                .collect(toList());
    }
}
