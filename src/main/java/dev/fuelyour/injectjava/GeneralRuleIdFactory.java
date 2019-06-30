package dev.fuelyour.injectjava;

class GeneralRuleIdFactory {

  private int unspecifiedIdCount = 0;

  GeneralRuleId buildGeneralRuleId() {
    return new GeneralRuleId(unspecifiedIdCount++);
  }

  GeneralRuleId buildGeneralRuleId(String name) {
    return new GeneralRuleId(name);
  }
}
