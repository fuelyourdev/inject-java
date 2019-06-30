package dev.fuelyour.injectjava;

import java.util.function.BiFunction;
import java.util.function.Function;

@SuppressWarnings({"WeakerAccess", "unused"})
public class GeneralRule<T> {

  private final TriFunction<
      SimpleContainer,
      Class<?>,
      String,
      Boolean> condition;
  private final TriFunction<
      SimpleContainer,
      Class<T>,
      String,
      ? extends T> rule;

  public GeneralRule(
      TriFunction<SimpleContainer, Class<?>, String, Boolean> condition,
      TriFunction<SimpleContainer, Class<T>, String, ? extends T> rule) {
    this.condition = condition;
    this.rule = (container, clazz, name) ->
        rule.apply(container, (Class<T>) clazz, name);
  }

  public GeneralRule(
      BiFunction<SimpleContainer, Class<?>, Boolean> condition,
      BiFunction<SimpleContainer, Class<T>, ? extends T> rule) {
    this.condition = (container, clazz, name) ->
        condition.apply(container, clazz);
    this.rule = (container, clazz, name) ->
        rule.apply(container, (Class<T>) clazz);
  }

  public GeneralRule(
      Function<Class<?>, Boolean> condition,
      Function<Class<T>, ? extends T> rule) {
    this.condition = (container, clazz, name) ->
        condition.apply(clazz);
    this.rule = (container, clazz, name) ->
        rule.apply((Class<T>) clazz);
  }

  boolean isConditionMet(
      SimpleContainer container,
      Class<?> clazz,
      String name) {
    return condition.apply(container, clazz, name);
  }

  T applyRule(SimpleContainer container, Class<T> clazz, String name) {
    return rule.apply(container, clazz, name);
  }
}
