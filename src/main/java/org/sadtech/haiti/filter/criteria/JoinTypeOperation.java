package org.sadtech.haiti.filter.criteria;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.criteria.JoinType;

/**
 * // TODO: 15.04.2021 Добавить описание.
 *
 * @author upagge 15.04.2021
 */
@Getter
@RequiredArgsConstructor
public enum JoinTypeOperation {

    LEFT(JoinType.LEFT),
    INNER(JoinType.INNER);

    private final JoinType criteriaType;

}
