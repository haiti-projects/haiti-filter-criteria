package dev.struchkov.haiti.filter.criteria;

import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

/**
 * // TODO: 09.11.2020 Добавить описание.
 *
 * @author upagge 09.11.2020
 */
public class Container<T> {

    private final String uuid = UUID.randomUUID().toString();

    private List<JoinTable> joinTables;
    private Specification<T> specification;

    private Container(List<JoinTable> joinTables) {
        this.joinTables = joinTables;
    }

    public static <T> Container<T> of(List<JoinTable> joinTables) {
        return new Container<>(joinTables);
    }

    public List<JoinTable> getJoinTables() {
        return joinTables;
    }

    public void setJoinTables(List<JoinTable> joinTables) {
        this.joinTables = joinTables;
    }

    public Specification<T> getSpecification() {
        return specification;
    }

    public void setSpecification(Specification<T> specification) {
        this.specification = specification;
    }

    public String getUuid() {
        return uuid;
    }

}
