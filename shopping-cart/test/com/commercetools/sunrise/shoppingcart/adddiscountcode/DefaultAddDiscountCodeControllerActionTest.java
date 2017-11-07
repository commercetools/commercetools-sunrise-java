package com.commercetools.sunrise.shoppingcart.adddiscountcode;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddDiscountCode;
import io.sphere.sdk.client.SphereClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DefaultAddDiscountCodeControllerAction}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultAddDiscountCodeControllerActionTest {
    public static final String DISCOUNT_CODE = "SUNNY";
    @Mock
    private SphereClient fakeSphereClient;
    @Mock
    private HookRunner fakeHookRunner;
    @Mock
    private Cart cart;
    @Captor
    private ArgumentCaptor<CartUpdateCommand> cartUpdateCommandCaptor;

    @InjectMocks
    private DefaultAddDiscountCodeControllerAction defaultAddDiscountCodeControllerAction;

    @Test
    public void execute() throws ExecutionException, InterruptedException {
        final DefaultAddDiscountCodeFormData addDiscountFormData = new DefaultAddDiscountCodeFormData();
        addDiscountFormData.setCode(DISCOUNT_CODE);

        mockHookRunnerThatReturnsCart();
        mockSphereClientThatReturnsCart();

        final Cart updatedCart = defaultAddDiscountCodeControllerAction.apply(cart, addDiscountFormData)
                .toCompletableFuture().get();

        assertThat(updatedCart).isNotNull();

        final CartUpdateCommand cartUpdateCommand = cartUpdateCommandCaptor.getValue();
        assertThat(cartUpdateCommand.getUpdateActions())
                .hasSize(1)
                .hasOnlyElementsOfType(AddDiscountCode.class);

        final AddDiscountCode addDiscountCode = (AddDiscountCode) cartUpdateCommand.getUpdateActions().get(0);
        assertThat(addDiscountCode.getCode()).isEqualTo(DISCOUNT_CODE);
    }

    private void mockSphereClientThatReturnsCart() {
        when(fakeSphereClient.execute(any()))
                .thenReturn(CompletableFuture.completedFuture(cart));
    }

    private void mockHookRunnerThatReturnsCart() {
        when(fakeHookRunner.runUnaryOperatorHook(any(), any(), cartUpdateCommandCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(2));
        when(fakeHookRunner.runActionHook(any(), any(), any()))
                .thenAnswer(invocation -> CompletableFuture.completedFuture(invocation.getArgument(2)));
    }
}
