package dev.fuelyour.injectjava;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

class TransformRuleBuilder {

  private final GeneralRuleIdFactory gRuleIdFactory;
  private final Map<GeneralRuleId, GeneralTransformRule<?>>
      rules = new HashMap<>();

  TransformRuleBuilder(GeneralRuleIdFactory gRuleIdFactory) {
    this.gRuleIdFactory = gRuleIdFactory;
  }

  @SuppressWarnings("unchecked")
  <T> void applyRules(SimpleContainer container, Id id, T t) {
    Class<?> clazz = id.getClassId();
    String name = id.getStringId();
    for (GeneralTransformRule<?> rule : rules.values()) {
      if (rule.isConditionMet(container, clazz, name, t)) {
        ((GeneralTransformRule<T>)rule)
            .applyRule(container, (Class<T>) clazz, name, t);
      }
    }
  }

  <T> void setTransformRule(Class<T> clazz, Consumer<T> rule) {
    setCoreTransformRule(clazz, (container, obj) ->
      rule.accept(obj)
    );
  }

  <T> void setTransformRule(
      String ruleName,
      Class<T> clazz,
      Consumer<T> rule) {
    setCoreTransformRule(ruleName, clazz, (container, obj) ->
      rule.accept(obj)
    );
  }

  <T> void setTransformRule(
      Class<T> clazz,
      BiConsumer<SimpleContainer, T> rule) {
    setCoreTransformRule(clazz, rule);
  }

  <T> void setTransformRule(
      String ruleName,
      Class<T> clazz,
      BiConsumer<SimpleContainer, T> rule) {
    setCoreTransformRule(ruleName, clazz, rule);
  }

  @SuppressWarnings("unchecked")
  <T> void setTransformRule(String name, Consumer<T> rule) {
    setCoreTransformRule(name, (container, obj) ->
      rule.accept((T) obj)
    );
  }

  @SuppressWarnings("unchecked")
  <T> void setTransformRule(
      String ruleName,
      String name,
      Consumer<T> rule) {
    setCoreTransformRule(ruleName, name, (container, obj) ->
      rule.accept((T) obj)
    );
  }

  @SuppressWarnings("unchecked")
  <T> void setTransformRule(String name, BiConsumer<SimpleContainer, T> rule) {
    setCoreTransformRule(name, (container, obj) ->
      rule.accept(container, (T) obj)
    );
  }

  @SuppressWarnings("unchecked")
  <T> void setTransformRule(
      String ruleName,
      String name,
      BiConsumer<SimpleContainer, T> rule) {
    setCoreTransformRule(ruleName, name, (container, obj) ->
      rule.accept(container, (T) obj));
  }

  void setGeneralTransformRule(
      GeneralTransformRule rule) {
    rules.put(gRuleIdFactory.buildGeneralRuleId(), rule);
  }

  void setGeneralTransformRule(
      String ruleName,
      GeneralTransformRule rule) {
    rules.put(gRuleIdFactory.buildGeneralRuleId(ruleName), rule);
  }

  @SuppressWarnings("unchecked")
  private <T> void setCoreTransformRule(
      Class<T> clazz,
      BiConsumer<SimpleContainer, T> rule) {
    rules.put(gRuleIdFactory.buildGeneralRuleId(),
        new GeneralTransformRule<>(
            (container, otherClass, name, inst) ->
              clazz == otherClass,
            (container, otherClass, name, inst) ->
              rule.accept(container, (T) inst)
            ));
  }

  @SuppressWarnings("unchecked")
  private <T> void setCoreTransformRule(
      String ruleName,
      Class<T> clazz,
      BiConsumer<SimpleContainer, T> rule) {
    rules.put(gRuleIdFactory.buildGeneralRuleId(ruleName),
        new GeneralTransformRule<>(
            (container, otherClass, name, inst) ->
                clazz == otherClass,
            (container, otherClass, name, inst) ->
                rule.accept(container, (T) inst)
        ));
  }

  @SuppressWarnings("unchecked")
  private <T> void setCoreTransformRule(
      String name,
      BiConsumer<SimpleContainer, T> rule) {
    rules.put(gRuleIdFactory.buildGeneralRuleId(),
        new GeneralTransformRule<>(
            (container, clazz, otherName, inst) ->
                name.equals(otherName),
            (container, otherClass, otherName, inst) ->
              rule.accept(container, (T) inst)
            ));
  }

  @SuppressWarnings("unchecked")
  private <T> void setCoreTransformRule(
      String ruleName,
      String name,
      BiConsumer<SimpleContainer, T> rule) {
    rules.put(gRuleIdFactory.buildGeneralRuleId(ruleName),
        new GeneralTransformRule<>(
            (container, clazz, otherName, inst) ->
                name.equals(otherName),
            (container, otherClass, otherName, inst) ->
                rule.accept(container, (T) inst)
        ));
  }
}
