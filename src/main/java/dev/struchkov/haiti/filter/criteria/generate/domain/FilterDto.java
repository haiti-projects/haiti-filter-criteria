package dev.struchkov.haiti.filter.criteria.generate.domain;

import dev.struchkov.haiti.filter.criteria.generate.annotation.GenerateFilter;

import java.util.List;

/**
 * // TODO: 18.04.2021 Добавить описание.
 *
 * @author upagge 18.04.2021
 */
public class FilterDto {

    private String name;
    private String packageName;
    private FilterType filterType;
    private GenerateFilter filterOption;
    private List<FilterParameter> parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public GenerateFilter getFilterOption() {
        return filterOption;
    }

    public void setFilterOption(GenerateFilter filterOption) {
        this.filterOption = filterOption;
    }

    public List<FilterParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<FilterParameter> parameters) {
        this.parameters = parameters;
    }

}
