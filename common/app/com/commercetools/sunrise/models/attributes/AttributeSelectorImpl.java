package com.commercetools.sunrise.models.attributes;

import com.commercetools.sunrise.core.viewmodels.ViewModelFactory;
import com.commercetools.sunrise.models.products.AttributeSelectOption;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Singleton
final class AttributeSelectorImpl extends ViewModelFactory implements AttributeSelector {

    private final AttributeSettings settings;
    private final ProductAttributeFormatter attributeFormatter;
    private final ProductAttributeSorter attributeSorter;

    @Inject
    AttributeSelectorImpl(final AttributeSettings settings, final ProductAttributeFormatter attributeFormatter,
                          final ProductAttributeSorter attributeSorter) {
        this.settings = settings;
        this.attributeFormatter = attributeFormatter;
        this.attributeSorter = attributeSorter;
    }

    @Override
    public List<AttributeSelectOption> options(final String attributeName, final ProductProjection selectedProduct,
                                      final ProductVariant selectedVariant) {
        final Comparator<Attribute> comparator = attributeSorter.compare(attributeName, selectedProduct.getProductType());
        return selectedProduct.getAllVariants().stream()
                .filter(variant -> variant.getAttribute(attributeName) != null)
                .collect(groupingBy(variant -> variant.getAttribute(attributeName)))
                .entrySet().stream()
                .sorted((entry1, entry2) -> comparator.compare(entry1.getKey(), entry2.getKey()))
                .map(entry -> createOption(attributeName, selectedProduct, selectedVariant, entry.getValue()))
                .collect(toList());
    }

    private AttributeSelectOption createOption(final String attributeName, final ProductProjection selectedProduct,
                                      final ProductVariant selectedVariant, final List<ProductVariant> candidates) {
        return findBestTargetVariant(attributeName, selectedVariant, candidates)
                .map(target -> createOption(attributeName, selectedProduct, selectedVariant, target))
                .orElseGet(() -> {
                    final ProductVariant target = fallbackTargetVariant(selectedProduct, candidates);
                    return createOption(attributeName, selectedProduct, selectedVariant, target);
                });
    }

    private AttributeSelectOption createOption(final String attributeName, final ProductProjection selectedProduct,
                                               final ProductVariant selectedVariant, final ProductVariant targetVariant) {
        final AttributeSelectOption option = new AttributeSelectOption();
        findLabel(attributeName, selectedProduct, targetVariant).ifPresent(option::setLabel);
        option.setValue(targetVariant.getId().toString());
        option.setSelected(haveSameAttribute(attributeName, targetVariant, selectedVariant));
        option.setDisabled(isDisabled(attributeName, selectedVariant, targetVariant));
        option.setVariant(targetVariant);
        option.setSecondary(settings.isSecondary(attributeName));
        return option;
    }

    private boolean isDisabled(final String attributeName, final ProductVariant selectedVariant,
                               final ProductVariant targetVariant) {
        return settings.isSecondary(attributeName) && !isBestMatch(attributeName, selectedVariant, targetVariant);
    }

    private Optional<String> findLabel(final String attributeName, final ProductProjection selectedProduct,
                                       final ProductVariant targetVariant) {
        return Optional.ofNullable(targetVariant.getAttribute(attributeName))
                .flatMap(attribute -> attributeFormatter.convert(attribute, selectedProduct.getProductType()));
    }

    private Optional<ProductVariant> findBestTargetVariant(final String attributeName, final ProductVariant selectedVariant,
                                                           final List<ProductVariant> candidates) {
        return candidates.stream()
                .filter(candidate -> isBestMatch(attributeName, selectedVariant, candidate))
                .findFirst();
    }

    private ProductVariant fallbackTargetVariant(final ProductProjection selectedProduct, final List<ProductVariant> candidates) {
        return candidates.stream()
                .findFirst()
                .orElseGet(selectedProduct::getMasterVariant);
    }

    private boolean isBestMatch(final String attributeName, final ProductVariant selectedVariant, final ProductVariant candidate) {
        final boolean sameVariant = candidate.getId().equals(selectedVariant.getId());
        return sameVariant || matchesAllOtherAttributes(attributeName, selectedVariant, candidate);
    }

    private boolean matchesAllOtherAttributes(final String attributeName, final ProductVariant selectedVariant, final ProductVariant candidate) {
        return settings.selectable().stream()
                .filter(otherAttributeName -> !otherAttributeName.equals(attributeName))
                .allMatch(otherAttributeName -> haveSameAttribute(otherAttributeName, candidate, selectedVariant));
    }

    private boolean haveSameAttribute(final String attributeName, final ProductVariant variant1,
                                      final ProductVariant variant2) {
        final Attribute attribute1 = variant1.getAttribute(attributeName);
        final Attribute attribute2 = variant2.getAttribute(attributeName);
        return Objects.equals(attribute1, attribute2);
    }
}
