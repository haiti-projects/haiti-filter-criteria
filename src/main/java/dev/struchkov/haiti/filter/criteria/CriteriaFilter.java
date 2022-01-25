package dev.struchkov.haiti.filter.criteria;

import dev.struchkov.haiti.filter.Filter;
import dev.struchkov.haiti.filter.FilterQuery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CriteriaFilter<T> implements Filter {

    private final List<Specification<T>> andSpecifications = new ArrayList<>();
    private final List<Specification<T>> orSpecifications = new ArrayList<>();
    private final List<Specification<T>> notSpecifications = new ArrayList<>();

    private CriteriaFilter() {
    }

    public static <T> Filter create() {
        return new CriteriaFilter<T>();
    }

    @Override
    public Filter and(FilterQuery filterQuery) {
        andSpecifications.addAll(getSpecification(filterQuery));
        return this;
    }

    @Override
    public Filter and(Consumer<FilterQuery> query) {
        final FilterQuery criteriaQuery = CriteriaQuery.create();
        query.accept(criteriaQuery);
        andSpecifications.addAll(getSpecification(criteriaQuery));
        return this;
    }

    @Override
    public Filter or(FilterQuery filterQuery) {
        orSpecifications.addAll(getSpecification(filterQuery));
        return this;
    }

    @Override
    public Filter or(Consumer<FilterQuery> query) {
        final FilterQuery criteriaQuery = CriteriaQuery.create();
        query.accept(criteriaQuery);
        orSpecifications.addAll(getSpecification(criteriaQuery));
        return this;
    }

    @Override
    public Filter not(FilterQuery filterQuery) {
        notSpecifications.addAll(
                getSpecification(filterQuery).stream()
                        .map(Specification::not)
                        .collect(Collectors.toUnmodifiableList())
        );
        return this;
    }

    @Override
    public Filter not(Consumer<FilterQuery> query) {
        final FilterQuery criteriaQuery = CriteriaQuery.create();
        query.accept(criteriaQuery);
        notSpecifications.addAll(
                getSpecification(criteriaQuery).stream()
                        .map(Specification::not)
                        .collect(Collectors.toUnmodifiableList())
        );
        return this;
    }

    private List<Specification<T>> getSpecification(FilterQuery filterQuery) {
        CriteriaQuery<T> criteriaQuery = (CriteriaQuery<T>) filterQuery;
        return criteriaQuery.build();
    }

    @Override
    public Specification<T> build() {
        return generateSpecification(
                generateQueryAnd(andSpecifications),
                generateQueryAnd(notSpecifications),
                generateQueryOr(orSpecifications)
        );
    }

    private Specification<T> generateQueryOr(List<Specification<T>> orSpecifications) {
        Specification<T> queryOr = null;
        if (!orSpecifications.isEmpty()) {
            queryOr = orSpecifications.get(0);
            if (orSpecifications.size() > 1) {
                for (int i = 1; i < orSpecifications.size(); i++) {
                    queryOr = queryOr.or(orSpecifications.get(i));
                }
            }
        }
        return queryOr;
    }

    private Specification<T> generateQueryAnd(List<Specification<T>> andSpecifications) {
        Specification<T> queryAnd = null;
        if (!andSpecifications.isEmpty()) {
            queryAnd = andSpecifications.get(0);
            if (andSpecifications.size() > 1) {
                for (int i = 1; i < andSpecifications.size(); i++) {
                    queryAnd = queryAnd.and(andSpecifications.get(i));
                }
            }
        }
        return queryAnd;
    }

    private Specification<T> generateSpecification(Specification<T>... specifications) {
        final List<Specification<T>> specificationList = Arrays.stream(specifications)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (!specificationList.isEmpty()) {
            Specification<T> query = Specification.where(specificationList.get(0));
            for (int i = 1; i < specificationList.size(); i++) {
                query = query.and(specificationList.get(i));
            }
            return query;
        }
        return null;
    }

}
