package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HandlebarsHook;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;

import javax.inject.Singleton;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@Singleton
public final class ProductAvailabilityComponent implements ControllerComponent, HandlebarsHook {

    @Override
    public CompletionStage<Handlebars> onHandlebarsCreated(final Handlebars handlebars) {
        return completedFuture(handlebars.registerHelper("availabilityColorCode", availabilityColorCodeHelper()));
    }

    private Helper<Long> availabilityColorCodeHelper() {
        return (availableQuantity, options) -> {
            final String code;
            if (availableQuantity < 4) {
                code = "red";
            } else if (availableQuantity > 10) {
                code = "green";
            } else {
                code = "orange";
            }
            return code;
        };
    }
}
