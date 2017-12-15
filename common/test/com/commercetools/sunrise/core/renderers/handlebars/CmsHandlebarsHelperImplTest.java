package com.commercetools.sunrise.core.renderers.handlebars;

import com.commercetools.sunrise.cms.CmsPage;
import com.commercetools.sunrise.core.i18n.I18nResolver;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Options;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;
import java.util.Optional;

import static com.commercetools.sunrise.core.renderers.handlebars.HandlebarsTemplateEngine.CMS_PAGE_IN_CONTEXT_KEY;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CmsHandlebarsHelperImplTest {

    private static final String MESSAGE_KEY = "foo.bar";
    private static final String MESSAGE_KEY_ANSWER = "OK";

    @Mock
    private CmsPage fakeCmsPage;
    @Mock
    private I18nResolver dummyI18nResolver;

    @InjectMocks
    private DefaultHandlebarsHelperSource helperSource;

    @Before
    public void setUp() throws Exception {
        when(fakeCmsPage.field(eq(MESSAGE_KEY))).thenReturn(Optional.of(MESSAGE_KEY_ANSWER));
    }

    @Test
    public void returnsCmsContent() throws Exception {
        assertThat(helperSource.cms(MESSAGE_KEY, optionsWithCmsPage())).isEqualTo(MESSAGE_KEY_ANSWER);
        verify(fakeCmsPage).field(MESSAGE_KEY);
    }

    @Test
    public void returnsEmptyOnUndefinedMessage() throws Exception {
        assertThat(helperSource.cms("unknown", optionsWithCmsPage())).isEmpty();
        verify(fakeCmsPage).field("unknown");
    }

    @Test
    public void returnsEmptyOnMissingCmsPage() throws Exception {
        assertThat(helperSource.cms(MESSAGE_KEY, optionsWithoutCmsPage())).isEmpty();
        verify(fakeCmsPage, never()).field(MESSAGE_KEY);
    }

    private Options optionsWithCmsPage() {
        final Map<String, CmsPage> contextModel = singletonMap(CMS_PAGE_IN_CONTEXT_KEY, fakeCmsPage);
        return optionsBuilder(contextModel)
                .build();
    }

    private Options optionsWithoutCmsPage() {
        return optionsBuilder(emptyMap())
                .build();
    }

    private static Options.Builder optionsBuilder(final Object contextModel) {
        final Context context = Context.newContext(contextModel);
        return new Options.Builder(null, null, null, context, null);
    }
}
