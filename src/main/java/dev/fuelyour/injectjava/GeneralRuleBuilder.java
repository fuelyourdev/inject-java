package dev.fuelyour.injectjava;

import java.util.LinkedHashMap;
import java.util.Map;

class GeneralRuleBuilder {

  private final GeneralRuleIdFactory gRuleIdFactory;
  private final Map<GeneralRuleId, GeneralRule<?>> rules =
      new LinkedHashMap<>();

  GeneralRuleBuilder(GeneralRuleIdFactory gRuleIdFactory) {
    this.gRuleIdFactory = gRuleIdFactory;
  }

  @SuppressWarnings("unchecked")
  <T> GeneralRule<T> getApplicableRule(SimpleContainer container, Id id) {
    for (GeneralRule<?> rule : rules.values()) {
      if (rule.isConditionMet(container, id.getClassId(), id.getStringId())) {
        return (GeneralRule<T>) rule;
      }
    }
    return null;
  }

  void setGeneralRule(
      String ruleName,
      GeneralRule<?> rule) {
    rules.put(gRuleIdFactory.buildGeneralRuleId(ruleName), rule);
  }

  void setGeneralRule(
      GeneralRule<?> rule) {
    rules.put(gRuleIdFactory.buildGeneralRuleId(), rule);
  }
}
