package io.sphere.sdk.play.setupwidget.models;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class SphereCredentials extends Base {
    @Constraints.Required
    @Constraints.MinLength(1)
    private String projectKey;
    @Constraints.Required
    @Constraints.MinLength(1)
    private String clientId;
    @Constraints.MinLength(1)
    @Constraints.Required
    private String clientSecret;

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
}
