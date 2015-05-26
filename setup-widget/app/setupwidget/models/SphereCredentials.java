package setupwidget.models;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class SphereCredentials extends Base {
    @Constraints.Required(message = "Project key is required")
    private String projectKey;

    @Constraints.Required(message = "Client ID is required")
    private String clientId;

    @Constraints.Required(message = "Client secret is required")
    private String clientSecret;

    public SphereCredentials() {
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setProjectKey(final String projectKey) {
        this.projectKey = projectKey;
    }

    public void setClientId(final String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(final String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
