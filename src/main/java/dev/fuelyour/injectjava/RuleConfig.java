package dev.fuelyour.injectjava;

import java.util.function.*;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public class RuleConfig {

  private final RuleBuilder ruleBuilder;
  private final GeneralRuleBuilder generalRuleBuilder;
  private final TransformRuleBuilder transformRuleBuilder;

  RuleConfig(
      RuleBuilder ruleBuilder,
      GeneralRuleBuilder generalRuleBuilder,
      TransformRuleBuilder transformRuleBuilder) {
    this.ruleBuilder = ruleBuilder;
    this.generalRuleBuilder = generalRuleBuilder;
    this.transformRuleBuilder = transformRuleBuilder;
  }

  public <T> RuleConfig setRule(Class<T> clazz, Supplier<T> rule) {
    ruleBuilder.setRule(clazz, rule);
    return this;
  }

  public <T> RuleConfig setRule(
      Class<T> clazz,
      Function<SimpleContainer, T> rule) {
    ruleBuilder.setRule(clazz, rule);
    return this;
  }

  public <T> RuleConfig setRule(String name, Supplier<T> rule) {
    ruleBuilder.setRule(name, rule);
    return this;
  }

  public <T> RuleConfig setRule(
      String name,
      Function<SimpleContainer, T> rule) {
    ruleBuilder.setRule(name, rule);
    return this;
  }

  public <T> RuleConfig setGeneralRule(
      String ruleName,
      GeneralRule<T> rule) {
    generalRuleBuilder.setGeneralRule(ruleName, rule);
    return this;
  }

  public <T> RuleConfig setGeneralRule(GeneralRule<T> rule) {
    generalRuleBuilder.setGeneralRule(rule);
    return this;
  }

  public <T> RuleConfig setTransformRule(Class<T> clazz, Consumer<T> rule) {
    transformRuleBuilder.setTransformRule(clazz, rule);
    return this;
  }

  public <T> RuleConfig setTransformRule(
      String ruleName,
      Class<T> clazz,
      Consumer<T> rule) {
    transformRuleBuilder.setTransformRule(ruleName, clazz, rule);
    return this;
  }

  public <T> RuleConfig setTransformRule(
      Class<T> clazz,
      BiConsumer<SimpleContainer, T> rule) {
    transformRuleBuilder.setTransformRule(clazz, rule);
    return this;
  }

  public <T> RuleConfig setTransformRule(
      String ruleName,
      Class<T> clazz,
      BiConsumer<SimpleContainer, T> rule) {
    transformRuleBuilder.setTransformRule(ruleName, clazz, rule);
    return this;
  }

  public <T> RuleConfig setTransformRule(String name, Consumer<T> rule) {
    transformRuleBuilder.setTransformRule(name, rule);
    return this;
  }

  public <T> RuleConfig setTransformRule(
      String ruleName,
      String name,
      Consumer<T> rule) {
    transformRuleBuilder.setTransformRule(ruleName, name, rule);
    return this;
  }

  public <T> RuleConfig setTransformRule(
      String name,
      BiConsumer<SimpleContainer, T> rule) {
    transformRuleBuilder.setTransformRule(name, rule);
    return this;
  }

  public <T> RuleConfig setTransformRule(
      String ruleName,
      String name,
      BiConsumer<SimpleContainer, T> rule) {
    transformRuleBuilder.setTransformRule(ruleName, name, rule);
    return this;
  }

  public <T> RuleConfig setGeneralTransformRule(GeneralTransformRule rule) {
    transformRuleBuilder.setGeneralTransformRule(rule);
    return this;
  }

  public <T> RuleConfig setGeneralTransformRule(
      String ruleName,
      GeneralTransformRule rule) {
    transformRuleBuilder.setGeneralTransformRule(ruleName, rule);
    return this;
  }
}
