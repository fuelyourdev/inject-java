package dev.fuelyour.injectjava;

public class GeneralTransformRule<T> {

  private final QuadFunction<
      SimpleContainer,
      Class<?>,
      String,
      Object,
      Boolean> condition;

  private final QuadConsumer<
      SimpleContainer,
      Class<T>,
      String,
      T> rule;

  public GeneralTransformRule(
      QuadFunction<SimpleContainer, Class<?>, String, Object, Boolean> condition,
      QuadConsumer<SimpleContainer, Class<T>, String, T> rule) {
    this.condition = condition;
    this.rule = rule;
  }

  boolean isConditionMet(
      SimpleContainer container,
      Class<?> clazz,
      String name,
      Object obj) {
    return condition.apply(container, clazz, name, obj);
  }

  void applyRule(
      SimpleContainer container,
      Class<T> clazz,
      String name,
      T t) {
    rule.accept(container, clazz, name, t);
  }
}
