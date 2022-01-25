package dev.struchkov.haiti.filter.criteria.generate.domain;

/**
 * // TODO: 18.04.2021 Добавить описание.
 *
 * @author upagge 18.04.2021
 */
public class FilterParameter {

    private String type;
    private String name;

    private FilterParameter(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static FilterParameter of(String type, String name) {
        return new FilterParameter(type, name);
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

}
