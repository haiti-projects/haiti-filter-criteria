package dev.struchkov.haiti.filter.criteria.generate.processor;

import com.google.auto.service.AutoService;
import dev.struchkov.haiti.filter.criteria.generate.annotation.FilterField;
import dev.struchkov.haiti.filter.criteria.generate.annotation.GenerateFilter;
import dev.struchkov.haiti.filter.criteria.generate.domain.FilterDto;
import dev.struchkov.haiti.filter.criteria.generate.domain.FilterParameter;
import dev.struchkov.haiti.utils.Strings;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static dev.struchkov.haiti.filter.criteria.generate.Annotations.getPackage;
import static dev.struchkov.haiti.filter.criteria.generate.Annotations.isField;

/**
 * @author upagge 17.04.2021
 */
@SupportedAnnotationTypes("dev.struchkov.haiti.filter.criteria.generate.annotation.GenerateFilter")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@AutoService(Processor.class)
public class GenerateFilterProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = env.getElementsAnnotatedWith(annotation);
            for (Element annotatedElement : annotatedElements) {
                final FilterDto filterDto = createFilter(annotatedElement);
                FilterRecorder.record(filterDto, processingEnv);
            }
        }
        return true;
    }

    private FilterDto createFilter(Element annotatedElement) {
        final TypeMirror typeMirror = annotatedElement.asType();
        final String simpleClassName = annotatedElement.getSimpleName().toString();
        final GenerateFilter filterOption = annotatedElement.getAnnotation(GenerateFilter.class);

        final FilterDto filterDto = new FilterDto();
        filterDto.setName(getFilterName(simpleClassName, filterOption));
        filterDto.setPackageName(getPackage(typeMirror));

        List<FilterParameter> parameters = new ArrayList<>();
        for (Element element : annotatedElement.getEnclosedElements()) {
            if (isField(element)) {
                fieldProcessing(element).ifPresent(parameters::add);
            }
        }
        filterDto.setParameters(parameters);
        return filterDto;
    }

    private Optional<FilterParameter> fieldProcessing(Element element) {
        final FilterField.Exclude exclude = element.getAnnotation(FilterField.Exclude.class);
        if (exclude == null) {
            final String simpleName = element.getSimpleName().toString();
            return Optional.of(FilterParameter.of(element.asType().toString(), simpleName));
        }
        return Optional.empty();
    }

    private String getFilterName(String className, GenerateFilter filterOption) {
        if (Strings.EMPTY.equals(filterOption.name())) {
            return className + "Filter";
        } else {
            return filterOption.name();
        }
    }

}
