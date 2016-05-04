package productcatalog.common;

import common.contexts.UserContext;
import common.models.ProductAttributeBean;
import common.models.ProductDataConfig;
import common.models.FormSelectableOptionBean;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.MetaProductType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static common.utils.ProductAttributeUtils.attributeValue;
import static common.utils.ProductAttributeUtils.attributeValueAsKey;
import static java.util.stream.Collectors.toList;

public class SelectableProductAttributeBean extends ProductAttributeBean {
    private boolean reload;
    private List<FormSelectableOptionBean> list = new ArrayList<>();
    private Map<String, Map<String, List<String>>> selectData = new HashMap<>();

    public SelectableProductAttributeBean() {
    }

    public SelectableProductAttributeBean(final Attribute selectedAttribute, final ProductProjection product,
                                          final ProductDataConfig productDataConfig, final UserContext userContext) {
        super(selectedAttribute, productDataConfig.getMetaProductType(), userContext);
        this.reload = productDataConfig.getHardSelectableAttributes().contains(selectedAttribute.getName());
        final MetaProductType metaProductType = productDataConfig.getMetaProductType();
        product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(selectedAttribute.getName()))
                .filter(attrOption -> attrOption != null)
                .distinct()
                .forEach(attrOption -> {
                    final String attrOptionValue = attributeValue(attrOption, metaProductType, userContext);
                    final String attrOptionValueKey = attributeValueAsKey(attrOptionValue);
                    this.list.add(new FormSelectableOptionBean(attrOptionValue, attrOptionValueKey, attrOption.equals(selectedAttribute)));
                    this.selectData.put(attrOptionValue, allowedAttributeCombinations(attrOption, product, productDataConfig, userContext));
                });
    }

    public boolean isReload() {
        return reload;
    }

    public void setReload(final boolean reload) {
        this.reload = reload;
    }

    public List<FormSelectableOptionBean> getList() {
        return list;
    }

    public void setList(final List<FormSelectableOptionBean> list) {
        this.list = list;
    }

    public Map<String, Map<String, List<String>>> getSelectData() {
        return selectData;
    }

    public void setSelectData(final Map<String, Map<String, List<String>>> selectData) {
        this.selectData = selectData;
    }

    private static Map<String, List<String>> allowedAttributeCombinations(final Attribute fixedAttribute, final ProductProjection product,
                                                                          final ProductDataConfig productDataConfig, final UserContext userContext) {
        final MetaProductType metaProductType = productDataConfig.getMetaProductType();
        final Map<String, List<String>> attrCombination = new HashMap<>();
        productDataConfig.getSelectableAttributes().stream()
                .filter(enabledAttrKey -> !fixedAttribute.getName().equals(enabledAttrKey))
                .forEach(enabledAttrKey -> {
                    final List<String> allowedAttrValues = attributeCombination(enabledAttrKey, fixedAttribute, product, metaProductType, userContext);
                    if (!allowedAttrValues.isEmpty()) {
                        attrCombination.put(enabledAttrKey, allowedAttrValues);
                    }
                });
        return attrCombination;
    }

    private static List<String> attributeCombination(final String attributeKey, final Attribute fixedAttribute,
                                                     final ProductProjection product, final MetaProductType metaProductType,
                                                     final UserContext userContext) {
        return product.getAllVariants().stream()
                .filter(variant -> variant.getAttribute(attributeKey) != null)
                .filter(variant -> {
                    final Attribute variantAttribute = variant.getAttribute(fixedAttribute.getName());
                    return variantAttribute != null && variantAttribute.equals(fixedAttribute);
                })
                .map(variant -> attributeValue(variant.getAttribute(attributeKey), metaProductType, userContext))
                .distinct()
                .collect(toList());
    }
}
