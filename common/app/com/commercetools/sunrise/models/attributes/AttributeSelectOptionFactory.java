package com.commercetools.sunrise.models.attributes;

import com.commercetools.sunrise.core.reverserouters.productcatalog.product.ProductReverseRouter;
import com.commercetools.sunrise.core.viewmodels.ViewModelFactory;
import com.commercetools.sunrise.models.SelectOption;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.*;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Singleton
public class AttributeSelectOptionFactory extends ViewModelFactory {

    private final AttributeSettings attributesSettings;
    private final ProductAttributeFormatter attributeFormatter;
    private final ProductAttributeSorter attributeSorter;
    private final ProductReverseRouter productReverseRouter;

    @Inject
    protected AttributeSelectOptionFactory(final AttributeSettings attributesSettings,
                                           final ProductAttributeFormatter attributeFormatter,
                                           final ProductAttributeSorter attributeSorter,
                                           final ProductReverseRouter productReverseRouter) {
        this.attributesSettings = attributesSettings;
        this.attributeFormatter = attributeFormatter;
        this.attributeSorter = attributeSorter;
        this.productReverseRouter = productReverseRouter;
    }

    public List<SelectOption> createList(final String attributeName, final ProductProjection selectedProduct,
                                         final ProductVariant selectedVariant) {
        final Comparator<Attribute> comparator = attributeSorter.comparator(attributeName, selectedProduct.getProductType());
        return selectedProduct.getAllVariants().stream()
                .collect(groupingBy(variant -> variant.getAttribute(attributeName)))
                .entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .sorted((entry1, entry2) -> comparator.compare(entry1.getKey(), entry2.getKey()))
                .map(entry -> create(attributeName, selectedProduct, selectedVariant, entry.getValue()))
                .collect(toList());
    }

    private SelectOption create(final String attributeName, final ProductProjection selectedProduct,
                                final ProductVariant selectedVariant, final List<ProductVariant> candidates) {
        return findBestTargetVariant(candidates, attributeName, selectedVariant)
                .map(target -> create(target, attributeName, selectedProduct, selectedVariant))
                .orElseGet(() -> {
                    final ProductVariant target = fallbackTargetVariant(candidates, selectedProduct);
                    return create(target, attributeName, selectedProduct, selectedVariant);
                });
    }

    public SelectOption create(final ProductVariant targetVariant, final String attributeName,
                               final ProductProjection selectedProduct, final ProductVariant selectedVariant) {
        final SelectOption option = new SelectOption();
        final boolean primaryAttribute = isPrimaryAttribute(attributeName);
        final boolean selected = haveSameAttribute(attributeName, targetVariant, selectedVariant);
        findLabel(targetVariant, attributeName, selectedProduct).ifPresent(option::setLabel);
        findValue(targetVariant, selectedProduct, primaryAttribute).ifPresent(option::setValue);
        option.setSelected(selected);
        option.setDisabled(isDisabled(targetVariant, attributeName, selectedVariant, primaryAttribute));
        return option;
    }

    private boolean isDisabled(final ProductVariant targetVariant, final String attributeName,
                               final ProductVariant selectedVariant, final boolean primaryAttribute) {
        return !primaryAttribute && !isBestMatch(targetVariant, attributeName, selectedVariant);
    }

    private Optional<String> findLabel(final ProductVariant targetVariant, final String attributeName,
                                       final ProductProjection selectedProduct) {
        return Optional.ofNullable(targetVariant.getAttribute(attributeName))
                .flatMap(attribute -> attributeFormatter.convert(attribute, selectedProduct.getProductType()));
    }

    private Optional<String> findValue(final ProductVariant targetVariant, final ProductProjection selectedProduct,
                                       final boolean primaryAttribute) {
        return primaryAttribute ? findUrl(selectedProduct, targetVariant) : Optional.of(targetVariant.getId().toString());
    }

    private Optional<String> findUrl(final ProductProjection product, final ProductVariant variant) {
        return productReverseRouter.productDetailPageCall(product, variant).map(Call::url);
    }

    private Optional<ProductVariant> findBestTargetVariant(final List<ProductVariant> candidates, final String attributeName,
                                                           final ProductVariant selectedVariant) {
        return candidates.stream()
                .filter(candidate -> isBestMatch(candidate, attributeName, selectedVariant))
                .findFirst();
    }

    private ProductVariant fallbackTargetVariant(final List<ProductVariant> candidates, final ProductProjection selectedProduct) {
        return candidates.stream()
                .findFirst()
                .orElseGet(selectedProduct::getMasterVariant);
    }

    private boolean isBestMatch(final ProductVariant candidate, final String attributeName, final ProductVariant selectedVariant) {
        final boolean sameVariant = candidate.getId().equals(selectedVariant.getId());
        return sameVariant || matchesAllOtherAttributes(candidate, attributeName, selectedVariant);
    }

    private boolean matchesAllOtherAttributes(final ProductVariant candidate, final String attributeName, final ProductVariant selectedVariant) {
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

    private boolean isPrimaryAttribute(final String selectedAttributeName) {
        return attributesSettings.primarySelectable().stream().anyMatch(selectedAttributeName::equals);
    }
}
