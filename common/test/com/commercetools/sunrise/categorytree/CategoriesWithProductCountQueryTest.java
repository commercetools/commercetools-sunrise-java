package com.commercetools.sunrise.categorytree;

import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.queries.QueryPredicate;
import io.sphere.sdk.queries.QuerySort;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoriesWithProductCountQueryTest {

    @Test
    public void changesVars() throws Exception {
        final List<QuerySort<CategoryWithProductCount>> sortExpressions = asList(QuerySort.of("sort by foo"), QuerySort.of("sort by bar"));
        final List<QueryPredicate<CategoryWithProductCount>> predicateExpressions = asList(QueryPredicate.of("filter by foo"), QueryPredicate.of("filter by bar"));
        final CategoriesWithProductCountQuery query = CategoriesWithProductCountQuery.of()
                .withOffset(5)
                .withLimit(40)
                .withSort(sortExpressions)
                .withPredicates(predicateExpressions);

        assertThat(query.offset()).isEqualTo(5);
        assertThat(query.limit()).isEqualTo(40);
        assertThat(query.sort()).isEqualTo(sortExpressions);
        assertThat(query.predicates()).isEqualTo(predicateExpressions);
    }

    @Test
    public void hasNullVarsByDefault() throws Exception {
        final CategoriesWithProductCountQuery query = CategoriesWithProductCountQuery.of();
        assertThat(query.offset()).isNull();
        assertThat(query.limit()).isNull();
        assertThat(query.sort()).isEmpty();
        assertThat(query.predicates()).isEmpty();
    }

    @Test
    public void keepsImmutability() throws Exception {
        final CategoriesWithProductCountQuery query1 = CategoriesWithProductCountQuery.of();
        final CategoriesWithProductCountQuery query2 = query1.withOffset(5);
        final CategoriesWithProductCountQuery query3 = query2.withLimit(40);
        assertThat(query1).isNotSameAs(query2).isNotSameAs(query3);
    }

    @Test
    public void generatesVariables() throws Exception {
        final List<QuerySort<CategoryWithProductCount>> sortExpressions = asList(QuerySort.of("sort by foo"), QuerySort.of("sort by bar"));
        final List<QueryPredicate<CategoryWithProductCount>> predicateExpressions = asList(QueryPredicate.of("filter by foo"), QueryPredicate.of("filter by bar"));
        final CategoriesWithProductCountQuery query = CategoriesWithProductCountQuery.of()
                .withOffset(5)
                .withLimit(40)
                .withSort(sortExpressions)
                .withPredicates(predicateExpressions);

        final JsonNode variables = query.getVariables();
        assertThat(variables).isNotNull();
        assertThat(variables.get("offset").asInt()).isEqualTo(5);
        assertThat(variables.get("limit").asInt()).isEqualTo(40);
        assertThat(variables.get("sort")).extracting(JsonNode::asText).containsExactly("sort by foo", "sort by bar");
        assertThat(variables.get("where").asText()).isEqualTo("filter by foo and filter by bar");
    }

    @Test
    public void findsQuery() throws Exception {
        assertThat(CategoriesWithProductCountQuery.of().getQuery())
                .isNotNull()
                .contains("categories");
    }
}
