package dev.fuelyour.injectjava.tests.container.apply.example2classes;

import dev.fuelyour.injectjava.Container;
import dev.fuelyour.injectjava.Module;

import static dev.fuelyour.injectjava.Container.buildContainer;
import static dev.fuelyour.injectjava.Rules.useConstructor;
import static dev.fuelyour.injectjava.Rules.withParams;

public class Ex2Main {

  private static Ex2Root root;

  public static void main(String[] args) {
    programMain(args, null);
  }

  public static void programMain(String[] args, Module testModule) {
    Container container = buildContainer();
    container.apply(rc -> rc.setRule(Ex2Root.class,
        useConstructor(Ex2Root.class,
            withParams(Ex2Dep1.class))));
    if (testModule != null) {
      container.apply(testModule);
    }
    root = container.get(Ex2Root.class);
  }

  public static Ex2Root getRoot() {
    return root;
  }
}
