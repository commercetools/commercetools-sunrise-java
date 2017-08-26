package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordPageContent;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels.RecoverPasswordPageContentFactory;
import com.google.common.collect.ImmutableMap;
import io.commercetools.sunrise.email.EmailDeliveryException;
import io.sphere.sdk.client.NotFoundException;
import io.sphere.sdk.customers.CustomerToken;
import io.sphere.sdk.utils.CompletableFutureUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import play.data.FormFactory;
import play.data.format.Formatters;
import play.i18n.Lang;
import play.i18n.MessagesApi;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.StatusHeader;

import javax.validation.Validator;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.CALLS_REAL_METHODS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SunriseRecoverPasswordController}.
 *
 * Instead of implementing a concrete {@link SunriseRecoverPasswordController},
 * this test is using mockito mock/spy to test that the abstract methods are called
 * with the correct parameters.
 *
 * TODO:
 * - May be we should try to reduce the number of used mocks?
 * - I'm not sure if this test makes sense, so let's talk about it ;-)
 */
public class SunriseRecoverPasswordControllerTest {
    @Mock
    private ContentRenderer contentRenderer;

    private FormFactory formFactory;
    @Mock
    private PageTitleResolver pageTitleResolver;
    @InjectMocks
    private  RecoverPasswordPageContentFactory pageContentFactory;

    @Mock
    private Validator validator;
    @Mock
    private MessagesApi messagesApi;
    @Mock
    private Formatters formatters;

    @Mock
    private Http.Context context;
    @Mock
    private CustomerToken customerToken;

    private Http.Request request;

    private  DefaultRecoverPasswordFormData formData;
    @Mock
    private  RecoverPasswordControllerAction controllerAction;

    private SunriseRecoverPasswordController recoverPasswordController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        formFactory = new FormFactory(messagesApi,formatters, validator);
        formData = new DefaultRecoverPasswordFormData();
        formData.setEmail("test@example.com");
        recoverPasswordController =
                Mockito.mock(SunriseRecoverPasswordController.class,
                        withSettings().useConstructor(contentRenderer, formFactory, pageContentFactory, formData, controllerAction)
                                .defaultAnswer(CALLS_REAL_METHODS));
    }

    @Test
    public void handleEmailDeliveryException() throws Exception {
        final Map<String, String> email = ImmutableMap.of("email", "test@example.com");
        request = new Http.RequestBuilder().bodyForm(email).build();
        Http.Context.current.set(context);
        final EmailDeliveryException emailDeliveryException = new EmailDeliveryException("Email delivery error");
        final StatusHeader badRequest = Results.badRequest();
        new RecoverPasswordPageContent();

        when:
        {
            when(context.request()).thenReturn(request);
            final CompletableFuture<CustomerToken> exceptionallyCompletedFuture =
                    CompletableFutureUtils.exceptionallyCompletedFuture(emailDeliveryException);
            when(controllerAction.apply(any()))
                    .thenReturn(exceptionallyCompletedFuture);
            when(recoverPasswordController.handleEmailDeliveryException(any(), eq(emailDeliveryException)))
                    .thenReturn(CompletableFuture.completedFuture(badRequest));
        }

        then:
        {
            final CompletableFuture<Result> completableResult =
                    (CompletableFuture<Result>) recoverPasswordController.process("en");
            final Result result = completableResult.get();
            assertThat(result).isEqualTo(badRequest);

            verify(recoverPasswordController).handleEmailDeliveryException(any(), eq(emailDeliveryException));
        }
    }
}
