package dev.fuelyour.injectjava;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

class RuleBuilder {

  private final IdFactory idFactory;
  private final Map<Id, Function<SimpleContainer, ?>> rules = new HashMap<>();

  RuleBuilder(
      IdFactory idFactory) {
    this.idFactory = idFactory;
  }

  boolean hasRule(Id id) {
    return rules.containsKey(id);
  }

  @SuppressWarnings("unchecked")
  <T> T build(SimpleContainer container, Id id) {
    return (T) rules.get(id).apply(container);
  }

  <T> void setRule(Class<T> clazz, Supplier<T> rule) {
    setRuleCore(clazz, container -> rule.get());
  }

  <T> void setRule(Class<T> clazz, Function<SimpleContainer, T> rule) {
    setRuleCore(clazz, rule);
  }

  <T> void setRule(String name, Supplier<T> rule) {
    setRuleCore(name, container -> rule.get());
  }

  <T> void setRule(String name, Function<SimpleContainer, T> rule) {
    setRuleCore(name, rule);
  }

  private <T> void setRuleCore(
      Class<T> clazz,
      Function<SimpleContainer, T> rule) {
    rules.put(idFactory.buildId(clazz), rule);
  }

  private <T> void setRuleCore(
      String name,
      Function<SimpleContainer, T> rule) {
    rules.put(idFactory.buildId(name), rule);
  }
}
