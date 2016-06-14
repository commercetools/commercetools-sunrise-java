package com.commercetools.sunrise.common;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import org.apache.commons.lang3.RandomStringUtils;
import play.Application;
import play.Configuration;
import play.Environment;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.ws.WS;
import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.mvc.Controller;
import play.mvc.Http;
import play.test.TestServer;

import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

public abstract class WithSunriseApplication {

    @FunctionalInterface
    public interface CheckedConsumer<T> {
        void apply(T t) throws Exception;
    }

    protected static void run(final Application app, final String url, final CheckedConsumer<WSRequest> test) {
        final TestServer server = testServer(3333, app);
        running(server, () -> {
            try {
                final WSClient wsClient = WS.newClient(server.port());
                test.apply(wsClient.url(url));
                wsClient.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected static <C extends Controller> void run(final Application app, final Class<C> controllerClass,
                                                     final CheckedConsumer<C> test) {
        running(app, () -> {
            try {
                test.apply(app.injector().instanceOf(controllerClass));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected Application app() {
        return appBuilder().build();
    }

    protected GuiceApplicationBuilder appBuilder(final Module ... modules) {
        final Module testModule = Modules.override(defaultModule()).with(modules);
        return new GuiceApplicationBuilder()
                .in(Environment.simple())
                .loadConfig(testConfiguration())
                .overrides(testModule);
    }

    protected DefaultTestModule defaultModule() {
        return new DefaultTestModule();
    }

    protected Configuration testConfiguration() {
        final Configuration configuration = Configuration.load(new Environment(Mode.TEST));
        final Map<String, Object> additionalSettings = new HashMap<>();
        additionalSettings.put("application.metrics.enabled", false);
        additionalSettings.put("application.settingsWidget.enabled", false);
        additionalSettings.put("application.auth.credentials", null);
        additionalSettings.put("play.crypto.secret", RandomStringUtils.randomAlphanumeric(5));
        return new Configuration(additionalSettings).withFallback(configuration);
    }

    protected static void setContext(final Http.Request request) {
        Http.Context.current.set(new Http.Context(request));
    }

    protected static Http.RequestBuilder requestBuilder() {
        return new Http.RequestBuilder().id(1L);
    }
}