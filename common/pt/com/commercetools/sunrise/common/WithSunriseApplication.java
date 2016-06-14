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
import play.test.TestBrowser;
import play.test.TestServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static play.test.Helpers.*;

public abstract class WithSunriseApplication {

    private static final int DEFAULT_PORT = 19001;

    @FunctionalInterface
    public interface CheckedConsumer<T> {
        void accept(T t) throws Exception;
    }

    @FunctionalInterface
    public interface CheckedBiConsumer<T, U> {
        void accept(T t, U u) throws Exception;
    }

    protected static void run(final Application app, final CheckedBiConsumer<TestBrowser, Integer> test) {
        final TestServer server = new TestServer(DEFAULT_PORT, app);
        running(server, HTMLUNIT, browser -> {
            try {
                test.accept(browser, server.port());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected static void run(final Application app, final String url, final CheckedConsumer<WSRequest> test) {
        final TestServer server = testServer(DEFAULT_PORT, app);
        running(server, () -> {
            final WSClient wsClient = WS.newClient(server.port());
            try {
                test.accept(wsClient.url(url));
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    wsClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected static <C extends Controller> void run(final Application app, final Class<C> controllerClass,
                                                     final CheckedConsumer<C> test) {
        running(app, () -> {
            try {
                test.accept(app.injector().instanceOf(controllerClass));
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