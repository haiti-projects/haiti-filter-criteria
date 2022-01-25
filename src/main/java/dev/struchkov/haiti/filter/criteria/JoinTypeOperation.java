package dev.struchkov.haiti.filter.criteria;

import javax.persistence.criteria.JoinType;

/**
 * @author upagge 15.04.2021
 */
public enum JoinTypeOperation {

    LEFT(JoinType.LEFT),
    INNER(JoinType.INNER);

    private final JoinType criteriaType;

    JoinTypeOperation(JoinType criteriaType) {
        this.criteriaType = criteriaType;
    }

    public JoinType getCriteriaType() {
        return criteriaType;
    }
}
