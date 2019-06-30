package dev.fuelyour.injectjava.exceptions;

import java.lang.reflect.Constructor;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

public class NoSuchConstructorException extends InjectJavaException {

  public NoSuchConstructorException(
      Class<?> classToBuild,
      Class<?>[] params,
      NoSuchMethodException cause) {
    super(constructMessage(classToBuild, params), cause);
  }

  private static String constructMessage(
      Class<?> classToBuild,
      Class<?>[] params) {
    return "The constructor " + constructorSignature(classToBuild, params) +
        " can't be found. " +
        (classToBuild.getConstructors().length > 0 ?
            "Did you mean to use one of these?\n" +
                constructorOptionsAndRules(classToBuild) :
            "No constructors are available..."
        );
  }

  private static String constructorOptionsAndRules(Class<?> classToBuild) {
    return stream(classToBuild.getConstructors())
        .map(constructor -> "  " +
            constructorSignature(
                classToBuild,
                constructor.getParameterTypes()) +
            " - try " + constructorRule(constructor))
        .collect(joining("\n"));
  }

  private static String constructorSignature(
      Class<?> classToBuild,
      Class<?>[] params) {
    return classToBuild.getSimpleName() + "(" + paramList(params) + ")";
  }

  private static String paramList(Class<?>[] params) {
    return stream(params)
        .map(Class::getSimpleName)
        .collect(joining(", "));
  }

  private static String constructorRule(Constructor<?> constructor) {
    return "container.setConstructorRule(" +
        constructor.getDeclaringClass().getSimpleName() + ".class" +
        (constructor.getParameterCount() > 0 ? ", " : "") +
        classList(constructor) +
        ")";
  }

  private static String classList(Constructor<?> constructor) {
    return stream(constructor.getParameterTypes())
        .map(p -> p.getSimpleName() + ".class")
        .collect(joining(", "));
  }
}
