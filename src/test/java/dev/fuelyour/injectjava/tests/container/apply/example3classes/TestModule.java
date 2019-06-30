package dev.fuelyour.injectjava.tests.container.apply.example3classes;

import dev.fuelyour.injectjava.Module;
import dev.fuelyour.injectjava.RuleConfig;

import static dev.fuelyour.injectjava.Rules.useConstructor;
import static dev.fuelyour.injectjava.Rules.withParams;

public class TestModule implements Module {

  @Override
  public void configure(RuleConfig rc) {
    rc.setRule(Ex3Root.class,
              useConstructor(Ex3Root.class,
                  withParams(Ex3Dep2.class)));
  }
}
