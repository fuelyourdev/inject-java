package dev.fuelyour.injectjava;

import dev.fuelyour.injectjava.exceptions.CircularDependencyException;
import dev.fuelyour.injectjava.exceptions.NoRuleException;

import java.util.ArrayList;
import java.util.List;

public class Container {

  private static ContainerFactory factory = null;

  public static Container buildContainer() {
    if (factory == null) {
      factory = new ContainerFactory();
    }
    return factory.build();
  }


  private final RuleBuilder ruleBuilder;
  private final GeneralRuleBuilder generalRuleBuilder;
  private final TransformRuleBuilder transformRuleBuilder;
  private final ConstructorBuilder constructorBuilder;
  private final ObjectManager objManager;
  private final IdFactory idFactory;
  private final RuleConfig ruleConfig;

  Container(
      IdFactory idFactory,
      ObjectManager objManager,
      RuleBuilder ruleBuilder,
      GeneralRuleBuilder generalRuleBuilder,
      TransformRuleBuilder transformRuleBuilder,
      ConstructorBuilder constructorBuilder,
      RuleConfig ruleConfig) {
    this.idFactory = idFactory;
    this.objManager = objManager;
    this.ruleBuilder = ruleBuilder;
    this.generalRuleBuilder = generalRuleBuilder;
    this.transformRuleBuilder = transformRuleBuilder;
    this.constructorBuilder = constructorBuilder;
    this.ruleConfig = ruleConfig;
  }

  public void apply(Module module) {
    module.configure(ruleConfig);
  }

  public <T> T get(Class<T> clazz) {
    return get(clazz, new ArrayList<>());
  }

  public <T> T get(String name) {
    return get(name, new ArrayList<>());
  }

  <T> T get(Class<T> clazz, List<Id> hist) {
    return get(idFactory.buildId(clazz), hist);
  }

  <T> T get(String name, List<Id> hist) {
    return get(idFactory.buildId(name), hist);
  }

  @SuppressWarnings("unchecked")
  <T> T get(Id id, List<Id> hist) {
    if (hist.contains(id)) {
      throw new CircularDependencyException(id, hist);
    }
    hist.add(id);
    if (!objManager.containsKey(id)) {
      T obj = build(id, hist);
      objManager.put(id, obj);
      transformRuleBuilder.applyRules(
          new SimpleContainer(this, new ArrayList<>()),
          id,
          obj);
    }
    hist.remove(hist.size() - 1);
    return (T) objManager.get(id);
  }

  boolean hasRule(Class<?> clazz) {
    return ruleBuilder.hasRule(idFactory.buildId(clazz));
  }

  boolean hasRule(String name) {
    return ruleBuilder.hasRule(idFactory.buildId(name));
  }

  @SuppressWarnings("unchecked")
  private <T> T build(Id id, List<Id> hist) {
    SimpleContainer simpleContainer = new SimpleContainer(this, hist);
    if (ruleBuilder.hasRule(id)) {
      return ruleBuilder.build(simpleContainer, id);
    } else {
      GeneralRule<T> rule =
          generalRuleBuilder.getApplicableRule(simpleContainer, id);
      if (rule != null) {
        return rule.applyRule(
            simpleContainer,
            (Class<T>) id.getClassId(),
            id.getStringId());
      } else {
        if (id.getStringId() != null) {
          if (id.getClassId() != null) {
            return get(idFactory.buildId(id.getClassId()), hist);
          } else {
            throw new NoRuleException(
                "No rule found for name: " + id.getStringId());
          }
        } else {
          return constructorBuilder.build(simpleContainer, id);
        }
      }
    }
  }

  <T> T build(Class<T> clazz, List<Id> hist, ParameterName[] paramNames) {
    SimpleContainer simpleContainer = new SimpleContainer(this, hist);
    return constructorBuilder.build(simpleContainer, clazz, paramNames);
  }

  <T> T buildWithConstructor(
      Class<T> clazz,
      List<Id> hist,
      Class<?>[] params,
      ParameterName[] paramNames) {
    SimpleContainer simpleContainer = new SimpleContainer(this, hist);
    return constructorBuilder.buildWithConstructor(
        simpleContainer,
        clazz,
        params,
        paramNames);
  }
}
