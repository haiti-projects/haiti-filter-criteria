package org.sadtech.haiti.filter.criteria;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sadtech.haiti.filter.FilterQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaQuery<T> implements FilterQuery {

    private static final CriteriaQuery<?> EMPTY = new CriteriaQuery<>(new ArrayList<>());
    private final List<Container<T>> specifications;
    private String joinTable;
    private final SimpleCriteriaQuery<T> simpleCriteriaQuery = new SimpleCriteriaQuery();

    public static <T> FilterQuery create() {
        return new CriteriaQuery<T>(new ArrayList<>());
    }

    /**
     * Используется для присоединения таблицы, все последующие операции {@link CriteriaQuery} будут выполняться для
     * этого Join
     *
     * @param fieldName Имя поля сущности, которое отвечает за название таблицы.
     */
    public CriteriaQuery<T> join(@NonNull String fieldName) {
        joinTable = fieldName;
        return this;
    }

    @Override
    public <Y extends Comparable<? super Y>> FilterQuery between(@NonNull String field, Y from, Y to) {
        if (from != null && to != null) {
            specifications.add(simpleCriteriaQuery.between(joinTable, field, from, to));
        }
        return this;
    }

    @Override
    public FilterQuery matchPhrase(@NonNull String field, Object value) {
        if (value != null) {
            specifications.add(simpleCriteriaQuery.matchPhrase(joinTable, field, value));
        }
        return this;
    }

    @Override
    public <U> FilterQuery matchPhrase(@NonNull String field, Set<U> values) {
        if (values != null && !values.isEmpty()) {
            specifications.addAll(
                    values.stream()
                            .map(value -> simpleCriteriaQuery.matchPhrase(joinTable, field, value))
                            .collect(Collectors.toList())
            );
        }
        return this;
    }

    @Override
    public FilterQuery exists(String field) {
        if (field != null) {
            specifications.add(simpleCriteriaQuery.exists(joinTable, field));
        }
        return this;
    }

    @Override
    public FilterQuery like(@NonNull String field, String value, boolean ignoreCase) {
        specifications.add(
                ignoreCase
                        ? simpleCriteriaQuery.likeIgnoreCase(joinTable, field, value)
                        : simpleCriteriaQuery.like(joinTable, field, value)
        );
        return this;
    }

    @Override
    public FilterQuery checkBoolInt(@NonNull String field, Boolean flag) {
        if (flag != null) {
            specifications.add(
                    simpleCriteriaQuery.between(joinTable, field, 0, Integer.MAX_VALUE)
            );
        }
        return this;
    }

    @Override
    public <Q> Q build() {
        return (Q) specifications;
    }

    public static <E> CriteriaQuery<E> empty() {
        final CriteriaQuery empty = EMPTY;
        return empty;
    }

}
