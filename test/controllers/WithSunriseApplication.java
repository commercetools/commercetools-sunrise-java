package controllers;

import io.sphere.sdk.client.PlayJavaClientImpl;
import io.sphere.sdk.client.SphereRequestExecutor;
import io.sphere.sdk.client.SphereRequestExecutorTestDouble;
import play.Application;
import play.Configuration;
import plugins.Global;
import play.test.FakeApplication;
import play.test.WithApplication;

import static play.test.Helpers.fakeApplication;

public class WithSunriseApplication extends WithApplication {
    @Override
    protected FakeApplication provideFakeApplication() {
        return fakeApplication(new Global() {
            @Override
            protected PlayJavaClientImpl createPlayJavaClient(Application app) {
                return new PlayJavaClientImpl(getConfiguration(app), getSphereRequestExecutor());
            }
        });
    }

    protected SphereRequestExecutor getSphereRequestExecutor() {
        return new SphereRequestExecutorTestDouble() {
        };
    }

    /**
     * Override this to add additional settings
     * @param app the application used
     * @return a configuration containing the {@code app} configuration values and overridden values
     */
    protected Configuration getConfiguration(Application app) {
        return app.configuration();
    }
}
