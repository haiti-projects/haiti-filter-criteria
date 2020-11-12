package org.sadtech.haiti.filter.criteria;

import lombok.NonNull;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.Map;

public class SimpleCriteriaQuery<T> {

    private final Map<String, Join> joinMap = new HashMap<>();

    public Container<T> matchPhrase(String joinTable, @NonNull String field, @NonNull Object value) {
        final Container<T> container = getContainer(joinTable);
        container.setSpecification((root, query, cb) -> cb.equal(getPath(root, joinTable, field), value));
        return container;
    }

    public Container<T> exists(String joinTable, @NonNull String field) {
        final Container<T> container = getContainer(joinTable);
        container.setSpecification((root, query, cb) -> cb.isNotNull(getPath(root, joinTable, field)));
        return container;
    }

    public Container<T> likeIgnoreCase(String joinTable, @NonNull String field, String value) {
        final Container<T> container = getContainer(joinTable);
        container.setSpecification((root, query, cb) -> cb.like(cb.lower(getPath(root, joinTable, field)), value));
        return container;
    }

    public Container<T> like(String joinTable, @NonNull String field, String value) {
        final Container<T> container = getContainer(joinTable);
        container.setSpecification((root, query, cb) -> cb.like(getPath(root, joinTable, field), value));
        return container;
    }

    public <Y extends Comparable<? super Y>> Container<T> between(String joinTable, @NonNull String field, Y from, Y to) {
        final Container<T> container = getContainer(joinTable);
        container.setSpecification((root, query, cb) -> cb.between(getPath(root, joinTable, field), from, to));
        return container;
    }

    private Container<T> getContainer(String joinTableName) {
        final Container<T> container = new Container<>();
        container.setJoinTable(joinTableName);
        return container;
    }

    private <Z> Expression<Z> getPath(Root<T> root, String joinTable, String field) {
        if (joinTable != null) {
            Join join = joinMap.get(joinTable);
            if (join == null) {
                join = root.join(joinTable);
                joinMap.put(joinTable, join);
            }
            return join.get(field);
        }
        return root.get(field);
    }

}
