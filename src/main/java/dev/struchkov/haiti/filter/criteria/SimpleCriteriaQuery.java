package dev.struchkov.haiti.filter.criteria;

import dev.struchkov.haiti.utils.Assert;
import org.hibernate.query.criteria.internal.path.PluralAttributeJoinSupport;

import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Attribute;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static dev.struchkov.haiti.utils.Assert.Utils.nullPointer;

public class SimpleCriteriaQuery<T> {

    public static final String FIELD = "field";

    private From<Object, Object> lastJoin;
    private final Set<String> unique = new HashSet<>();
    private final Map<String, From<Object, Object>> joinMap = new HashMap<>();

    public Container<T> matchPhrase(List<JoinTable> joinTables, String field, Object value) {
        Assert.isNotNull(field, nullPointer(FIELD));
        Assert.isNotNull(value, nullPointer("value"));

        final Container<T> container = Container.of(joinTables);
        container.setSpecification((root, query, cb) -> cb.equal(getPath(root, container).get(field), value));
        return container;
    }

    public <U> Container<T> collectionElements(List<JoinTable> joinTables, List<U> values) {
        final Container<T> container = Container.of(joinTables);
        container.setSpecification(
                (root, query, builder) -> {
                    Predicate where = builder.conjunction();
                    return builder.and(where, getPath(root, container).in(values));
                }
        );
        return container;
    }

    public Container<T> exists(List<JoinTable> joinTables, String field) {
        Assert.isNotNull(field, nullPointer(FIELD));

        final Container<T> container = Container.of(joinTables);
        container.setSpecification((root, query, cb) -> cb.isNotNull(getPath(root, container).get(field)));
        return container;
    }

    public Container<T> likeIgnoreCase(List<JoinTable> joinTables, String field, String value) {
        Assert.isNotNull(field, nullPointer(FIELD));

        final Container<T> container = Container.of(joinTables);
        container.setSpecification((root, query, cb) -> cb.like(cb.lower(getPath(root, container).get(field)), value));
        return container;
    }

    public Container<T> like(List<JoinTable> joinTables, String field, String value) {
        Assert.isNotNull(field, nullPointer(FIELD));

        final Container<T> container = Container.of(joinTables);
        container.setSpecification((root, query, cb) -> cb.like(getPath(root, container).get(field), value));
        return container;
    }

    public <Y extends Comparable<? super Y>> Container<T> between(List<JoinTable> joinTables, String field, Y from, Y to) {
        Assert.isNotNull(field, nullPointer(FIELD));

        final Container<T> container = Container.of(joinTables);
        container.setSpecification((root, query, cb) -> cb.between(getPath(root, container).get(field), from, to));
        return container;
    }

    public <Y extends Comparable<? super Y>> Container<T> greaterThan(List<JoinTable> joinTables, String field, Y value) {
        Assert.isNotNull(field, nullPointer(FIELD));

        final Container<T> container = Container.of(joinTables);
        container.setSpecification(((root, query, cb) -> cb.greaterThan(getPath(root, container).get(field), value)));
        return container;
    }

    public <Y extends Comparable<? super Y>> Container<T> lessThan(List<JoinTable> joinTables, String field, Y value) {
        Assert.isNotNull(field, nullPointer(FIELD));

        final Container<T> container = Container.of(joinTables);
        container.setSpecification(((root, query, cb) -> cb.lessThan(getPath(root, container).get(field), value)));
        return container;
    }

    private From<Object, Object> getPath(Root root, Container<T> container) {
        final List<JoinTable> joinTables = container.getJoinTables();
        final String uuid = container.getUuid();
        From<Object, Object> join = root;
        if (!joinTables.isEmpty()) {
            check(uuid);
            for (JoinTable table : joinTables) {
                final String tableName = table.getTableName();
                final JoinType criteriaType = table.getJoinTypeOperation().getCriteriaType();
                if (joinMap.containsKey(tableName)) {
                    join = joinMap.get(tableName);
                } else {
                    final Set<String> attributes = ((Root<Object>) root).getModel().getAttributes().stream()
                            .map(Attribute::getName)
                            .collect(Collectors.toSet());
                    if (attributes.contains(tableName)) {
                        join = root.join(tableName, criteriaType);
                    } else {
                        join = lastJoin.join(tableName, criteriaType);
                    }
                    if (join instanceof PluralAttributeJoinSupport && !((PluralAttributeJoinSupport) join).isBasicCollection()) {
                        lastJoin = join;
                    }
                    joinMap.put(tableName, join);
                }
            }
        }
        return join;
    }

    private void check(String uuid) {
        if (unique.contains(uuid)) {
            joinMap.clear();
        }
        unique.add(uuid);
    }

}
