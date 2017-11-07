package controllers.shoppingcart;

import com.commercetools.sunrise.it.WithSphereClient;
import com.commercetools.sunrise.sessions.cart.CartInSession;
import io.sphere.sdk.cartdiscounts.*;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.LineItemDraft;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddDiscountCode;
import io.sphere.sdk.client.BlockingSphereClient;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.discountcodes.DiscountCode;
import io.sphere.sdk.discountcodes.DiscountCodeDraft;
import io.sphere.sdk.discountcodes.DiscountCodeDraftBuilder;
import io.sphere.sdk.models.DefaultCurrencyUnits;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.PriceDraft;
import io.sphere.sdk.products.ProductDraft;
import io.sphere.sdk.products.ProductVariantDraft;
import io.sphere.sdk.products.ProductVariantDraftBuilder;
import io.sphere.sdk.taxcategories.TaxCategoryDraft;
import io.sphere.sdk.taxcategories.TaxCategoryDraftBuilder;
import io.sphere.sdk.utils.MoneyImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import play.mvc.Result;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static com.commercetools.sunrise.it.CartDiscountTestFixtures.deleteCartDiscountAndDiscountCodes;
import static com.commercetools.sunrise.it.CartDiscountTestFixtures.withCartDiscount;
import static com.commercetools.sunrise.it.CartTestFixtures.withCart;
import static com.commercetools.sunrise.it.DiscountCodeTestFixtures.withDiscountCode;
import static com.commercetools.sunrise.it.ProductTestFixtures.*;
import static com.commercetools.sunrise.it.ProductTypeTestFixtures.productTypeDraft;
import static com.commercetools.sunrise.it.ProductTypeTestFixtures.withProductType;
import static com.commercetools.sunrise.it.TaxCategoryTestFixtures.withTaxCategory;
import static com.commercetools.sunrise.it.TestFixtures.randomString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static play.inject.Bindings.bind;
import static play.test.Helpers.*;

@RunWith(MockitoJUnitRunner.class)
public class RemoveDiscountCodeControllerIntegrationTest extends WithSphereClient {
    @Mock
    private CartInSession cartInSession;

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder()
                .overrides(
                        bind(SphereClient.class).toInstance(sphereClient),
                        bind(CartInSession.class).toInstance(cartInSession)
                ).build();
    }

    @Before
    public void deleteExistingCartDiscountAndDiscountCodes() {
        deleteCartDiscountAndDiscountCodes(sphereClient, "CartDiscountValue", "0.6");
    }

    @Test
    public void shouldRemoveDiscountCode() throws Exception {
        withCartDiscountAndDiscountCode(sphereClient, discountCode -> {
            withTaxedFilledAndDiscountedCart(sphereClient, discountCode, cart -> {
                when(cartInSession.findCartId()).thenReturn(Optional.of(cart.getId()));

                final Map<String, String> bodyForm = new HashMap<>();
                bodyForm.put("discountCodeId", discountCode.getId());
                final Result result = route(new Http.RequestBuilder()
                        .uri("/en/cart/discount/remove")
                        .method(POST)
                        .bodyForm(bodyForm));

                assertThat(result.status()).isEqualTo(SEE_OTHER);
                assertThat(result.redirectLocation())
                        .isPresent()
                        .hasValue("/en/cart");

                return cart;
            });

            return discountCode;
        });
    }

    private static void withTaxedFilledAndDiscountedCart(final BlockingSphereClient client, final DiscountCode discountCode, final Function<Cart, Cart> test) {
        final TaxCategoryDraft taxCategoryDraft =
                TaxCategoryDraftBuilder.of(randomString(), Collections.emptyList(), null)
                        .build();
        withTaxCategory(client, taxCategoryDraft, taxCategory -> {
            withProductType(client, productTypeDraft(), productType -> {
                final ProductVariantDraft productVariantDraft = ProductVariantDraftBuilder.of()
                        .price(PriceDraft.of(BigDecimal.TEN, DefaultCurrencyUnits.EUR))
                        .build();
                final ProductDraft productDraft = productDraft(productType, productVariantDraft())
                        .publish(true)
                        .taxCategory(taxCategory)
                        .masterVariant(productVariantDraft)
                        .build();
                withProduct(client, productDraft, product -> {
                    final LineItemDraft lineItemDraft = LineItemDraft.of(product, 1, 1L);
                    final CartDraft cartDraft = CartDraftBuilder.of(DefaultCurrencyUnits.EUR)
                            .lineItems(Arrays.asList(lineItemDraft))
                            .build();
                    withCart(client, cartDraft, cart -> {
                        final Cart discountedCart = sphereClient.executeBlocking(CartUpdateCommand.of(cart, AddDiscountCode.of(discountCode.getCode())));

                        return test.apply(discountedCart);
                    });
                    return product;
                });
                return productType;
            });

            return taxCategory;
        });
    }

    private static void withCartDiscountAndDiscountCode(final BlockingSphereClient client, final Function<DiscountCode, DiscountCode> test) {
        final CartDiscountValue discountValue = AbsoluteCartDiscountValue.of(MoneyImpl.ofCents(100, DefaultCurrencyUnits.EUR));
        final CartDiscountTarget cartDiscountTarget = LineItemsTarget.of("1=1");
        final CartDiscountDraft cartDiscountDraft =
                CartDiscountDraftBuilder
                        .of("1=1", LocalizedString.ofEnglish("CartDiscount"), true, "0.6", cartDiscountTarget, discountValue)
                        .build();
        withCartDiscount(sphereClient, cartDiscountDraft, cartDiscount -> {
            final DiscountCodeDraft discountCodeDraft =
                    DiscountCodeDraftBuilder
                            .of(randomString(), cartDiscount)
                            .build();
            withDiscountCode(sphereClient, discountCodeDraft, test);
            return cartDiscount;
        });
    }
}
