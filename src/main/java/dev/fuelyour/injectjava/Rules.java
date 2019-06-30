package dev.fuelyour.injectjava;

import java.util.function.Function;
import java.util.function.Supplier;

public class Rules {

  public static <T, U extends T> Supplier<T> useInstance(U instance) {
    return () -> instance;
  }

  public static <T, U extends T> Function<SimpleContainer, T> get(
      Class<U> clazz) {
    return c -> c.get(clazz);
  }

  public static <T> Function<SimpleContainer, T> get(
      String name) {
    return c -> c.get(name);
  }

  public static <T, U extends T> Function<SimpleContainer, T> useConstructor(
      Class<U> clazz,
      Class<?>[] params) {
    return c -> c.buildWithConstructor(clazz, params);
  }

  public static <T, U extends T> Function<SimpleContainer, T> useConstructor(
      Class<U> clazz,
      Class<?>[] params,
      ParameterName[] paramNames) {
    return c -> c.buildWithConstructor(clazz, params, paramNames);
  }

  public static <T, U extends T>
  Function<SimpleContainer, T> useSoleConstructor(
      Class<U> clazz) {
    return c -> c.build(clazz);
  }

  public static <T, U extends T>
  Function<SimpleContainer, T> useSoleConstructor(
      Class<U> clazz,
      ParameterName[] paramNames) {
    return c -> c.build(clazz, paramNames);
  }

  public static Class<?>[] withParams(Class<?>... params) {
    return params;
  }

  public static ParameterName[] withParamNames(ParameterName... paramNames) {
    return paramNames;
  }

  public static ParameterName paramName(String name, Class<?> clazz) {
    return new ParameterName(name, clazz);
  }

  public static ParameterName paramName(
      String name,
      Class<?> clazz,
      int occurrence) {
    return new ParameterName(name, clazz, occurrence);
  }
}
