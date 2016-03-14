package shoppingcart.cartdetail;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class RemoveLineItemFormData extends Base {
    @Constraints.Required
    private String lineItemId;

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(final String lineItemId) {
        this.lineItemId = lineItemId;
    }
}
