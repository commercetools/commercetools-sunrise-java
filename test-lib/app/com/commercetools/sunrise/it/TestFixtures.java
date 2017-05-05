package com.commercetools.sunrise.it;

import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.client.ConcurrentModificationException;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Versioned;

import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

public final class TestFixtures {

    private static final Random RANDOM = new Random();
    public static final int DEFAULT_DELETE_TTL = 5;

    public static <T> void deleteWithRetry(final BlockingSphereClient client, final Versioned<T> resource,
                                           final Function<Versioned<T>, SphereRequest<T>> deleteFunction, final int ttl) {
        if (ttl > 0) {
            try {
                client.executeBlocking(deleteFunction.apply(resource));
            } catch (final ConcurrentModificationException e) {
                if (e.getCurrentVersion() != null) {
                    final Versioned<T> resourceWithCurrentVersion = Versioned.of(resource.getId(), e.getCurrentVersion());
                    deleteWithRetry(client, resourceWithCurrentVersion, deleteFunction, ttl - 1);
                }
            } catch (final NotFoundException e) {
                // mission indirectly accomplished
            }
        } else {
            throw new RuntimeException("Could not delete resource due to too many concurrent updates, resource: " + resource);
        }
    }

    public static String randomKey() {
        return "random-slug-" + RANDOM.nextInt();
    }

    public static String randomEmail(final Class<?> clazz) {
        return "random-email-" + RANDOM.nextInt() + "-" + clazz.getSimpleName() + "@test.commercetools.de";
    }

    public static String randomString() {
        return "random string " + RANDOM.nextInt() + System.currentTimeMillis();
    }

    public static LocalizedString randomLocalizedKey() {
        return LocalizedString.of(Locale.ENGLISH, randomKey());
    }
}
