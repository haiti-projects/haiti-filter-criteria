package org.sadtech.haiti.filter.criteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.UUID;

/**
 * // TODO: 09.11.2020 Добавить описание.
 *
 * @author upagge 09.11.2020
 */
@Getter
@Setter
@NoArgsConstructor
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

}
