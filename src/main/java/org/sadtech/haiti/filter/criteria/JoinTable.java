package org.sadtech.haiti.filter.criteria;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * // TODO: 15.04.2021 Добавить описание.
 *
 * @author upagge 15.04.2021
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinTable {

    private final String tableName;
    private final JoinTypeOperation joinTypeOperation;

    public static JoinTable of(@NonNull String tableName) {
        return new JoinTable(tableName, JoinTypeOperation.LEFT);
    }

    public static JoinTable of(@NonNull String tableName, JoinTypeOperation joinType) {
        return new JoinTable(tableName, joinType);
    }

}
