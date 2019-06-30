package dev.fuelyour.injectjava.testclasses;

import dev.fuelyour.injectjava.Container;

public class ClassWithContainer {

  private final Container container;

  public ClassWithContainer() {
    container = Container.buildContainer();
  }

  public Container getContainer() {
    return container;
  }
}
