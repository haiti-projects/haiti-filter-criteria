package dev.struchkov.haiti.filter.criteria;

import dev.struchkov.haiti.utils.Inspector;

import static dev.struchkov.haiti.utils.Inspector.Utils.nullPointer;

/**
 * @author upagge 15.04.2021
 */
public class JoinTable {

    private final String tableName;
    private final JoinTypeOperation joinTypeOperation;

    private JoinTable(String tableName, JoinTypeOperation joinTypeOperation) {
        this.tableName = tableName;
        this.joinTypeOperation = joinTypeOperation;
    }

    public static JoinTable of(String tableName) {
        Inspector.isNotNull(tableName, nullPointer("tableName"));
        return new JoinTable(tableName, JoinTypeOperation.LEFT);
    }

    public static JoinTable of(String tableName, JoinTypeOperation joinType) {
        Inspector.isNotNull(tableName, nullPointer("tableName"));
        return new JoinTable(tableName, joinType);
    }

    public String getTableName() {
        return tableName;
    }

    public JoinTypeOperation getJoinTypeOperation() {
        return joinTypeOperation;
    }
}
