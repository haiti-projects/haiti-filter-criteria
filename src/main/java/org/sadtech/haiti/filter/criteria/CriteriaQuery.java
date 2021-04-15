package org.sadtech.haiti.filter.criteria;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.sadtech.haiti.filter.FilterQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CriteriaQuery<T> implements FilterQuery {

    private static final CriteriaQuery<?> EMPTY = new CriteriaQuery<>(new ArrayList<>());
    private final List<Container<T>> containers;
    private List<JoinTable> joinTables = new ArrayList<>();
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
    public CriteriaQuery<T> join(@NonNull String... fieldName) {
        joinTables = Arrays.stream(fieldName)
                .map(JoinTable::of)
                .collect(Collectors.toList());
        return this;
    }

    public CriteriaQuery<T> join(@NonNull JoinTable... joinTables) {
        this.joinTables = Arrays.stream(joinTables).collect(Collectors.toList());
        return this;
    }

    @Override
    public <Y extends Comparable<? super Y>> FilterQuery between(@NonNull String field, Y from, Y to) {
        if (from != null && to != null) {
            containers.add(simpleCriteriaQuery.between(joinTables, field, from, to));
        }
        return this;
    }

    @Override
    public <Y extends Comparable<? super Y>> FilterQuery greaterThan(@NonNull String field, Y value) {
        if (value != null) {
            containers.add(simpleCriteriaQuery.greaterThan(joinTables, field, value));
        }
        return this;
    }

    @Override
    public <Y extends Comparable<? super Y>> FilterQuery lessThan(@NonNull String field, Y value) {
        if (value != null) {
            containers.add(simpleCriteriaQuery.lessThan(joinTables, field, value));
        }
        return this;
    }

    @Override
    public FilterQuery matchPhrase(@NonNull String field, Object value) {
        if (value != null) {
            containers.add(simpleCriteriaQuery.matchPhrase(joinTables, field, value));
        }
        return this;
    }

    @Override
    public <U> FilterQuery matchPhrase(@NonNull String field, Set<U> values) {
        if (values != null && !values.isEmpty()) {
            containers.addAll(
                    values.stream()
                            .map(value -> simpleCriteriaQuery.matchPhrase(joinTables, field, value))
                            .collect(Collectors.toList())
            );
        }
        return this;
    }

    public <U extends List<U>> CriteriaQuery<T> collectionElementsIn(List<U> values) {
        if (values != null && !values.isEmpty()) {
            containers.addAll(
                    values.stream()
                            .map(value -> simpleCriteriaQuery.collectionElements(joinTables, value))
                            .collect(Collectors.toList())
            );
        }
        return this;
    }

    @Override
    public FilterQuery exists(String field) {
        if (field != null) {
            containers.add(simpleCriteriaQuery.exists(joinTables, field));
        }
        return this;
    }

    @Override
    public FilterQuery like(@NonNull String field, String value, boolean ignoreCase) {
        containers.add(
                ignoreCase
                        ? simpleCriteriaQuery.likeIgnoreCase(joinTables, field, value)
                        : simpleCriteriaQuery.like(joinTables, field, value)
        );
        return this;
    }

    @Override
    public FilterQuery checkBoolInt(@NonNull String field, Boolean flag) {
        if (flag != null) {
            containers.add(
                    simpleCriteriaQuery.between(joinTables, field, 0, Integer.MAX_VALUE)
            );
        }
        return this;
    }

    @Override
    public <Q> Q build() {
        return (Q) containers.stream()
                .map(Container::getSpecification)
                .collect(Collectors.toList());
    }

    public static <E> CriteriaQuery<E> empty() {
        final CriteriaQuery empty = EMPTY;
        return empty;
    }

}
