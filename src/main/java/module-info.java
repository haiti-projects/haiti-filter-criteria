module haiti.filter.criteria {
    exports dev.struchkov.haiti.filter.criteria;

    requires java.persistence;
    requires haiti.utils;
    requires spring.data.jpa;
    requires org.hibernate.orm.core;
    requires haiti.filter;
    requires java.compiler;
    requires com.google.auto.service;
}