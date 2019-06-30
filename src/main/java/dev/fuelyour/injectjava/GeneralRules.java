package dev.fuelyour.injectjava;

import java.lang.reflect.InvocationTargetException;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.reflect.Modifier.isAbstract;
import static java.util.Arrays.stream;

@SuppressWarnings("unused")
public class GeneralRules {

  public static <T> GeneralRule<T> useDefaultConstructor() {
    return new GeneralRule<>(
        clazz -> !clazz.isInterface() && !isAbstract(clazz.getModifiers()) &&
          stream(clazz.getConstructors())
              .anyMatch(ctor -> ctor.getParameters().length == 0),
        clazz -> {
          try {
            return clazz.getConstructor().newInstance();
          } catch (NoSuchMethodException |
              InstantiationException |
              IllegalAccessException |
              InvocationTargetException e) {
            return null;
          }
        }
    );
  }

  @SuppressWarnings("unchecked")
  public static <T> GeneralRule<T> useInterfaceImplRule() {
    return new GeneralRule<>(
        (container, clazz) -> {
          if (clazz.isInterface() || isAbstract(clazz.getModifiers())) {
            try {
              Class.forName(clazz.getName() + "Impl");
              return true;
            } catch (ClassNotFoundException e) {
              return false;
            }
          }
          return false;
        },
        (container, clazz) -> {
          try {
            Class<?> implClass = Class.forName(clazz.getName() + "Impl");
            return (T) container.get(implClass);
          } catch (ClassNotFoundException e) {
            return null;
          }
        }
    );
  }

  public static <T> GeneralRule<T> generalRule(
      TriFunction<SimpleContainer, Class<?>, String, Boolean> condition,
      TriFunction<SimpleContainer, Class<T>, String, ? extends T> rule) {
    return new GeneralRule<>(condition, rule);
  }

  public static <T> GeneralRule<T> generalRule(
      BiFunction<SimpleContainer, Class<?>, Boolean> condition,
      BiFunction<SimpleContainer, Class<T>, ? extends T> rule) {
    return new GeneralRule<>(condition, rule);
  }

  public static <T> GeneralRule<T> generalRule(
      Function<Class<?>, Boolean> condition,
      Function<Class<T>, ? extends T> rule) {
    return new GeneralRule<>(condition, rule);
  }
}