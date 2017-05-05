package com.commercetools.sunrise.search;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import org.junit.Test;

import java.util.Locale;
import java.util.Optional;

import static com.commercetools.sunrise.search.SearchUtils.localizeExpression;
import static com.commercetools.sunrise.search.SearchUtils.replaceCategoryExternalId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SearchUtilsTest {

    @Test
    public void replacesLocale() throws Exception {
        final String expression = localizeExpression("foo.bar {{locale}} desc", Locale.ENGLISH);
        assertThat(expression).isEqualTo("foo.bar en desc");
    }

    @Test
    public void replacesMultipleLocaleOccurrences() throws Exception {
        final String expression = localizeExpression("{{locale}}.foo.{{locale}} asc", Locale.ENGLISH);
        assertThat(expression).isEqualTo("en.foo.en asc");
    }

    @Test
    public void ignoresNoLocaleOccurrences() throws Exception {
        final String expression = localizeExpression("foo.bar", Locale.ENGLISH);
        assertThat(expression).isEqualTo("foo.bar");
    }

    @Test
    public void replacesCategoryExternalId() throws Exception {
        final Category category = mock(Category.class);
        when(category.getId()).thenReturn("some-id");
        final CategoryTree categoryTree = mock(CategoryTree.class);
        when(categoryTree.findByExternalId("some-external-id")).thenReturn(Optional.of(category));
        final String expression = replaceCategoryExternalId("foo.bar {{externalId=some-external-id}} desc", categoryTree);
        assertThat(expression).isEqualTo("foo.bar some-id desc");
    }

    @Test
    public void replacesMultipleCategoryExternalIdOccurrences() throws Exception {
        final Category category1 = mock(Category.class);
        final Category category2 = mock(Category.class);
        when(category1.getId()).thenReturn("some-id");
        when(category2.getId()).thenReturn("some-other-id");
        final CategoryTree categoryTree = mock(CategoryTree.class);
        when(categoryTree.findByExternalId("some-external-id")).thenReturn(Optional.of(category1));
        when(categoryTree.findByExternalId("some-other-external-id")).thenReturn(Optional.of(category2));
        final String expression = replaceCategoryExternalId("foo.{{externalId=some-external-id}} desc.{{externalId=some-other-external-id}}", categoryTree);
        assertThat(expression).isEqualTo("foo.some-id desc.some-other-id");
    }

    @Test
    public void ignoresNoCategoryExternalIdOccurrences() throws Exception {
        final CategoryTree categoryTree = mock(CategoryTree.class);
        when(categoryTree.findByExternalId(any())).thenReturn(Optional.empty());
        final String expression = replaceCategoryExternalId("foo.bar", categoryTree);
        assertThat(expression).isEqualTo("foo.bar");
    }

    @Test
    public void throwsExceptionIfNoCategoryExternalIdFound() throws Exception {
        final CategoryTree categoryTree = mock(CategoryTree.class);
        when(categoryTree.findByExternalId(any())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> replaceCategoryExternalId("foo.bar {{externalId=some-external-id desc}}", categoryTree))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("not found")
                .hasMessageContaining("some-external-id");
    }
}