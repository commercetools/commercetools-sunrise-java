package purchase;

import io.sphere.sdk.models.Base;

public class PaymentsBean extends Base {
    private String type;

    public PaymentsBean() {
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
