package dev.fuelyour.injectjava;

@FunctionalInterface
public interface Module {
  void configure(RuleConfig ruleConfig);
}
