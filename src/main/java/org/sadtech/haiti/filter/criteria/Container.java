package org.sadtech.haiti.filter.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.Specification;

/**
 * // TODO: 09.11.2020 Добавить описание.
 *
 * @author upagge 09.11.2020
 */
@Getter
@Setter
public class Container<T> {

    private String joinTable;
    private Specification<T> specification;

}
