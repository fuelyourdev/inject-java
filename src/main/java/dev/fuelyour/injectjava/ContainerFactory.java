package dev.fuelyour.injectjava;

class ContainerFactory {

  Container build() {
    IdFactory idFactory = new IdFactory();
    ObjectManager objManager = new ObjectManager();
    ConstructorBuilder constructorBuilder =
        new ConstructorBuilder(idFactory);
    RuleBuilder ruleBuilder =
        new RuleBuilder(idFactory);
    GeneralRuleIdFactory gRuleIdFactory = new GeneralRuleIdFactory();
    GeneralRuleBuilder generalRuleBuilder =
        new GeneralRuleBuilder(gRuleIdFactory);
    TransformRuleBuilder transformRuleBuilder =
        new TransformRuleBuilder(gRuleIdFactory);
    RuleConfig ruleConfig = new RuleConfig(
       ruleBuilder,
       generalRuleBuilder,
       transformRuleBuilder);
    return new Container(
        idFactory,
        objManager,
        ruleBuilder,
        generalRuleBuilder,
        transformRuleBuilder,
        constructorBuilder,
        ruleConfig);
  }
}
