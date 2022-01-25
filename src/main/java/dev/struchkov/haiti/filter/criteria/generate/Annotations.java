package dev.struchkov.haiti.filter.criteria.generate;

import dev.struchkov.haiti.utils.Exceptions;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;

/**
 * @author upagge 18.04.2021
 */
public final class Annotations {

    public static final String TAB = "    ";

    private Annotations() {
        Exceptions.utilityClass();
    }

    public static boolean isField(Element element) {
        return element != null && element.getKind().isField();
    }

    public static String getPackage(TypeMirror typeMirror) {
        final String[] split = typeMirror.toString().split("\\.");
        return String.join(".", Arrays.copyOf(split, split.length - 1));
    }

    public static String getter(int tabCount, String returnType, String name) {
        return tabs(tabCount) + "public " + returnType + " get" + name + "() " +
                tabs(tabCount + 1) + "{ " +
                tabs(tabCount + 2) + "return " + name + ";" +
                tabs(tabCount + 1) + "}";
    }

    private static String tabs(int tabCount) {
        StringBuilder tabs = new StringBuilder(TAB);
        for (int i = 1; i < tabCount; i++) {
            tabs.append(TAB);
        }
        return tabs.toString();
    }

}
