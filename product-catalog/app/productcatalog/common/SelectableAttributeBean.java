package productcatalog.common;

import common.contexts.UserContext;
import common.models.AttributeBean;
import common.models.SelectableData;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.MetaProductType;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class SelectableAttributeBean extends AttributeBean {
    private List<SelectableData> list;
    private Map<String, List<String>> selectData;

    public SelectableAttributeBean() {
    }

    public SelectableAttributeBean(final Attribute attribute, final ProductProjection product,
                                   final MetaProductType metaProductType, final UserContext userContext) {
        super(attribute, metaProductType, userContext);
        this.list = product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(attribute.getName()))
                .filter(attr -> attr != null)
                .map(attr -> formatValue(metaProductType, attr, userContext))
                .distinct()
                .map(attr -> new SelectableData(attr, attr))
                .collect(toList());
    }

    public List<SelectableData> getList() {
        return list;
    }

    public void setList(final List<SelectableData> list) {
        this.list = list;
    }

    public Map<String, List<String>> getSelectData() {
        return selectData;
    }

    public void setSelectData(final Map<String, List<String>> selectData) {
        this.selectData = selectData;
    }
}
