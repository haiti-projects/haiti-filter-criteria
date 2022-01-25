package dev.struchkov.haiti.filter.criteria.generate.processor;

import dev.struchkov.haiti.filter.criteria.generate.Annotations;
import dev.struchkov.haiti.filter.criteria.generate.domain.FilterDto;
import dev.struchkov.haiti.filter.criteria.generate.domain.FilterParameter;
import dev.struchkov.haiti.utils.Exceptions;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FilterRecorder {

    private FilterRecorder() {
        Exceptions.utilityClass();
    }

    public static void record(FilterDto filter, ProcessingEnvironment environment) {
        JavaFileObject builderFile = null;
        try {
            builderFile = environment.getFiler().createSourceFile(filter.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + filter.getPackageName() + ";");
            out.println();
            out.print("public class " + filter.getName() + " {");
            out.println();
            out.println();
            generateName(out, filter);
            out.println("    public static " + filter.getName() + " empty() {");
            out.println("        return new " + filter.getName() + "();");
            out.println("    }");
            out.println();
            out.println("}");
            out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateName(PrintWriter out, FilterDto filter) {
        final List<FilterParameter> parameters = filter.getParameters();
        for (FilterParameter parameter : parameters) {
            out.println(Annotations.getter(1, parameter.getType(), parameter.getName()));
            out.println();
        }
    }

}
