package dev.fuelyour.injectjava.tests.container.apply.example3classes;

import dev.fuelyour.injectjava.Container;
import dev.fuelyour.injectjava.Module;

import java.lang.reflect.Constructor;

import static dev.fuelyour.injectjava.Container.buildContainer;
import static dev.fuelyour.injectjava.Rules.useConstructor;
import static dev.fuelyour.injectjava.Rules.withParams;

public class Ex3Main {

  private static Ex3Root root;

  public static Ex3Root getRoot() {
    return root;
  }

  public static void main(String[] args) {
    Container container = buildContainer();
    container.apply(rc -> rc.setRule(Ex3Root.class,
        useConstructor(Ex3Root.class,
            withParams(Ex3Dep1.class))));
    if (args.length > 0) {
      String moduleClassName = args[0];
      applyTestModule(container, moduleClassName);
    }
    root = container.get(Ex3Root.class);
  }

  @SuppressWarnings("unchecked")
  private static void applyTestModule(
      Container container,
      String moduleClassName) {
    try {
      Class<? extends Module> clazz =
          (Class<? extends Module>) Class.forName(moduleClassName);
      Constructor<? extends Module> ctor = clazz.getConstructor();
      Module testModule = ctor.newInstance();
      container.apply(testModule);
    } catch (Exception ignored) { }
  }
}
