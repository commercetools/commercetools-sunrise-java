package basicauth;

import com.google.inject.AbstractModule;
import com.google.inject.util.Providers;

import javax.annotation.Nullable;

public class BasicAuthTestModule extends AbstractModule {
    @Nullable
    private final BasicAuth basicAuth;

    public BasicAuthTestModule(@Nullable final BasicAuth basicAuth) {
        this.basicAuth = basicAuth;
    }

    @Override
    protected void configure() {
        bind(BasicAuth.class).toProvider(Providers.of(basicAuth));
    }
}