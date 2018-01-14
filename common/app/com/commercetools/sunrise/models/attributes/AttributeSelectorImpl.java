package com.commercetools.sunrise.models.attributes;

import com.commercetools.sunrise.core.viewmodels.ViewModelFactory;
import com.commercetools.sunrise.models.SelectOption;
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

    private final AttributeSettings attributesSettings;
    private final ProductAttributeFormatter attributeFormatter;
    private final ProductAttributeSorter attributeSorter;

    @Inject
    AttributeSelectorImpl(final AttributeSettings attributesSettings, final ProductAttributeFormatter attributeFormatter,
                          final ProductAttributeSorter attributeSorter) {
        this.attributesSettings = attributesSettings;
        this.attributeFormatter = attributeFormatter;
        this.attributeSorter = attributeSorter;
    }

    @Override
    public List<SelectOption> options(final String attributeName, final ProductProjection selectedProduct,
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

    private SelectOption createOption(final String attributeName, final ProductProjection selectedProduct,
                                      final ProductVariant selectedVariant, final List<ProductVariant> candidates) {
        return findBestTargetVariant(attributeName, selectedVariant, candidates)
                .map(target -> createOption(attributeName, selectedProduct, selectedVariant, target))
                .orElseGet(() -> {
                    final ProductVariant target = fallbackTargetVariant(selectedProduct, candidates);
                    return createOption(attributeName, selectedProduct, selectedVariant, target);
                });
    }

    private SelectOption createOption(final String attributeName, final ProductProjection selectedProduct,
                                      final ProductVariant selectedVariant, final ProductVariant targetVariant) {
        final SelectOption option = new SelectOption();
        final boolean primaryAttribute = isPrimaryAttribute(attributeName);
        final boolean selected = haveSameAttribute(attributeName, targetVariant, selectedVariant);
        findLabel(attributeName, selectedProduct, targetVariant).ifPresent(option::setLabel);
        findValue(selectedProduct, targetVariant, primaryAttribute).ifPresent(option::setValue);
        option.setSelected(selected);
        option.setDisabled(isDisabled(attributeName, selectedVariant, targetVariant, primaryAttribute));
        return option;
    }

    private boolean isDisabled(final String attributeName, final ProductVariant selectedVariant,
                               final ProductVariant targetVariant, final boolean primaryAttribute) {
        return !primaryAttribute && !isBestMatch(attributeName, selectedVariant, targetVariant);
    }

    private Optional<String> findLabel(final String attributeName, final ProductProjection selectedProduct,
                                       final ProductVariant targetVariant) {
        return Optional.ofNullable(targetVariant.getAttribute(attributeName))
                .flatMap(attribute -> attributeFormatter.convert(attribute, selectedProduct.getProductType()));
    }

    private Optional<String> findValue(final ProductProjection selectedProduct, final ProductVariant targetVariant,
                                       final boolean primaryAttribute) {
        return primaryAttribute ? findUrl(selectedProduct, targetVariant) : Optional.of(targetVariant.getId().toString());
    }

    private Optional<String> findUrl(final ProductProjection product, final ProductVariant variant) {
//        return productReverseRouter.productDetailPageCall(product, variant).map(Call::url);
        return Optional.empty();
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
        return attributesSettings.selectable().stream()
                .filter(otherAttributeName -> !otherAttributeName.equals(attributeName))
                .allMatch(otherAttributeName -> haveSameAttribute(otherAttributeName, candidate, selectedVariant));
    }

    private boolean haveSameAttribute(final String attributeName, final ProductVariant variant1,
                                      final ProductVariant variant2) {
        final Attribute attribute1 = variant1.getAttribute(attributeName);
        final Attribute attribute2 = variant2.getAttribute(attributeName);
        return Objects.equals(attribute1, attribute2);
    }

    private boolean isPrimaryAttribute(final String attributeName) {
        return attributesSettings.primarySelectable().stream().anyMatch(attributeName::equals);
    }
}
