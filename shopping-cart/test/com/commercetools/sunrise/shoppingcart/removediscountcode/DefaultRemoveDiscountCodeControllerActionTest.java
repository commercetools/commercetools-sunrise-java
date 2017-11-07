package com.commercetools.sunrise.shoppingcart.removediscountcode;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.RemoveDiscountCode;
import io.sphere.sdk.client.SphereClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link DefaultRemoveDiscountCodeControllerAction}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultRemoveDiscountCodeControllerActionTest {
    @Mock
    private SphereClient fakeSphereClient;
    @Mock
    private HookRunner fakeHookRunner;
    @Mock
    private Cart cart;
    @Captor
    private ArgumentCaptor<CartUpdateCommand> cartUpdateCommandCaptor;

    @InjectMocks
    private DefaultRemoveDiscountCodeControllerAction defaultRemoveDiscountCodeControllerAction;

    @Test
    public void execute() throws ExecutionException, InterruptedException {
        final String discountCodeId = UUID.randomUUID().toString();

        final DefaultRemoveDiscountCodeFormData removeDiscountCodeFormData = new DefaultRemoveDiscountCodeFormData();
        removeDiscountCodeFormData.setDiscountCodeId(discountCodeId);

        mockHookRunnerThatReturnsCart();
        mockSphereClientThatReturnsCart();

        final Cart updatedCart = defaultRemoveDiscountCodeControllerAction.apply(cart, removeDiscountCodeFormData)
                .toCompletableFuture().get();

        assertThat(updatedCart).isNotNull();

        final CartUpdateCommand cartUpdateCommand = cartUpdateCommandCaptor.getValue();
        assertThat(cartUpdateCommand.getUpdateActions())
                .hasSize(1)
                .hasOnlyElementsOfType(RemoveDiscountCode.class);

        final RemoveDiscountCode removeDiscountCode = (RemoveDiscountCode) cartUpdateCommand.getUpdateActions().get(0);
        assertThat(removeDiscountCode.getDiscountCode().getId()).isEqualTo(discountCodeId);
    }

    private void mockHookRunnerThatReturnsCart() {
        when(fakeHookRunner.runUnaryOperatorHook(any(), any(), cartUpdateCommandCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(2));
        when(fakeHookRunner.runActionHook(any(), any(), any()))
                .thenAnswer(invocation -> CompletableFuture.completedFuture(invocation.getArgument(2)));
    }

    private void mockSphereClientThatReturnsCart() {
        when(fakeSphereClient.execute(any()))
                .thenReturn(CompletableFuture.completedFuture(cart));
    }
}
