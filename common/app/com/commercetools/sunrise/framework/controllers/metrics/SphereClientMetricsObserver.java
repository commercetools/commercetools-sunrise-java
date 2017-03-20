package com.commercetools.sunrise.framework.controllers.metrics;

import io.sphere.sdk.client.metrics.ObservedTotalDuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

final class SphereClientMetricsObserver implements Observer {

    private final List<ObservedTotalDuration> metrics = new ArrayList<>();

    @Override
    public void update(final Observable o, final Object arg) {
        if (arg instanceof ObservedTotalDuration) {
            metrics.add((ObservedTotalDuration) arg);
        }
    }

    public List<ObservedTotalDuration> getMetrics() {
        return metrics;
    }
}
