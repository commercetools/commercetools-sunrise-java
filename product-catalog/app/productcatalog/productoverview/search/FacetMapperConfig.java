package productcatalog.productoverview.search;

import io.sphere.sdk.models.Base;

import java.util.List;

public class FacetMapperConfig extends Base {

    private final String type;
    private final List<String> values;

    public FacetMapperConfig(final String type, final List<String> values) {
        this.type = type;
        this.values = values;
    }

    public String getType() {
        return type;
    }

    public List<String> getValues() {
        return values;
    }
}
